(ns health-it-exercise.android.api
  (:require [goog.crypt.base64 :as b64]))

(def token nil)

(defn login [id secret succ-fn err-fn]
  (let [form-body #js []]
    (.push form-body (str (js/encodeURIComponent "grant_type")
                          "="
                          (js/encodeURIComponent "client_credentials")))

    (-> (js/fetch "https://exerciseofmik.aidbox.io/oauth/token"
                  (clj->js {:method "POST"
                            :headers {"Authorization" (str "Basic " (b64/encodeString (str id ":" secret)))
                                      "Content-Type" "application/x-www-form-urlencoded"}
                            :body (.join form-body "&")}))
        (.then #(.json %))
        (.then #(if (nil? (.-access_token %))
                  (err-fn)
                  (do
                    (set! token (.-access_token %))
                    (succ-fn))))
        (.catch #(err-fn)))))

(defn load-patients [succ-fn err-fn]
  (-> (js/fetch (str "https://exerciseofmik.aidbox.io/fhir/Patient?access_token=" (js/encodeURIComponent token))
                (clj->js {:method "GET"}))
      (.then #(.json %))
      (.then #(succ-fn (js->clj (.-entry %) :keywordize-keys true)))
      (.catch #(err-fn))))

(defn create-patient [patient succ-fn err-fn]
  (-> (js/fetch (str "https://exerciseofmik.aidbox.io/fhir/Patient?access_token=" (js/encodeURIComponent token))
                (clj->js {:method "POST"
                          :headers {"Content-Type" "application/json"}
                          :body (.stringify js/JSON (clj->js patient))}))
      (.then #(.json %))
      (.then #(if (nil? (.-id %)) (err-fn) (succ-fn)))
      (.catch #(err-fn))))

