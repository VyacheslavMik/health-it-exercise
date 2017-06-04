(ns health-it-exercise.events
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [reg-event-db after]]
   [clojure.spec.alpha :as s]
   [health-it-exercise.db :as db :refer [app-db]]
   [health-it-exercise.android.utils :as utils]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    ;; (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _]
   app-db))

(reg-event-db
 :set-client-id
 validate-spec
 (fn [db [_ value]]
   (assoc db :client-id value)))

(reg-event-db
 :set-client-secret
 validate-spec
 (fn [db [_ value]]
   (assoc db :client-secret value)))

(reg-event-db
 :add-patient
 validate-spec
 (fn [db [_ value]]
   (assoc db :patients (vec (conj (:patients db) (r/atom value))))))

(reg-event-db
 :add-name
 validate-spec
 (fn [db [_]]
   (update-in db [:adding-patient :name] #(vec (conj % (:name db))))))

(reg-event-db
 :remove-name
 validate-spec
 (fn [db [_ i]]
   (update-in db [:adding-patient :name] #(utils/vec-remove % i))))

(reg-event-db
 :set-name-use
 validate-spec
 (fn [db [_ value]]
   (update-in db [:name :use] (fn [_] value))))

(reg-event-db
 :set-name-text
 validate-spec
 (fn [db [_ value]]
   (update-in db [:name :text] (fn [_] value))))

(reg-event-db
 :set-name-family
 validate-spec
 (fn [db [_ value]]
   (update-in db [:name :family] (fn [_] [value]))))

(reg-event-db
 :set-given-name
 validate-spec
 (fn [db [_ value]]
   (assoc db :given-name value)))

(reg-event-db
 :add-given-name
 validate-spec
 (fn [db [_]]
   (let [value (:given-name db)]
     (update-in db [:name :given] #(if (utils/not-blank? value) (vec (conj % value)) %)))))

(reg-event-db
 :remove-given-name
 validate-spec
 (fn [db [_ i]]
   (update-in db [:name :given] #(utils/vec-remove % i))))

(reg-event-db
 :set-prefix-name
 validate-spec
 (fn [db [_ value]]
   (assoc db :prefix-name value)))

(reg-event-db
 :add-prefix-name
 validate-spec
 (fn [db [_]]
   (let [value (:prefix-name db)]
     (update-in db [:name :prefix] #(if (utils/not-blank? value) (vec (conj % value)) %)))))

(reg-event-db
 :remove-prefix-name
 validate-spec
 (fn [db [_ i]]
   (update-in db [:name :prefix] #(utils/vec-remove % i))))

(reg-event-db
 :set-suffix-name
 validate-spec
 (fn [db [_ value]]
   (assoc db :suffix-name value)))

(reg-event-db
 :add-suffix-name
 validate-spec
 (fn [db [_]]
   (let [value (:suffix-name db)]
     (update-in db [:name :suffix] #(if (utils/not-blank? value) (vec (conj % value)) %)))))

(reg-event-db
 :remove-suffix-name
 validate-spec
 (fn [db [_ i]]
   (update-in db [:name :suffix] #(utils/vec-remove % i))))

(defn check-period [start-in end-in]
  (let [start (if (empty? start-in) nil (js/Date. start-in))
        end (if (empty? end-in) nil (js/Date. end-in))]
    (cond
      (and (nil? start) (not (nil? end)))
      [end-in end-in]

      (and (not-any? nil? [start end]) (> (goog.date.Date/compare start end) 0))
      [start-in start-in]

      :else [start-in end-in])))

(reg-event-db
 :set-name-period-start
 validate-spec
 (fn [db [_ value]]
   (let [[start end] (check-period value (get-in db [:name :period :end]))]
     (-> db
         (update-in [:name :period :start] (fn [] start))
         (update-in [:name :period :end] (fn [] end))))))

(reg-event-db
 :set-name-period-end
 validate-spec
 (fn [db [_ value]]
   (let [[start end] (check-period (get-in db [:name :period :start]) value)]
     (-> db
         (update-in [:name :period :start] (fn [] start))
         (update-in [:name :period :end] (fn [] end))))))

(reg-event-db
 :set-contact-point-system
 validate-spec
 (fn [db [_ value]]
   (update-in db [:contact-point :system] (fn [_] value))))

(reg-event-db
 :set-contact-point-value
 validate-spec
 (fn [db [_ value]]
   (update-in db [:contact-point :value] (fn [_] value))))

(reg-event-db
 :set-contact-point-use
 validate-spec
 (fn [db [_ value]]
   (update-in db [:contact-point :use] (fn [_] value))))

(defn check-rank [value]
  (cond (pos? (js/parseInt value)) (js/parseInt value) (empty? value) 1 :else 1))

(reg-event-db
 :set-contact-point-rank
 validate-spec
 (fn [db [_ value]]
   (update-in db [:contact-point :rank] (fn [_] (check-rank value)))))

(reg-event-db
 :set-contact-point-period-start
 validate-spec
 (fn [db [_ value]]
   (let [[start end] (check-period value (get-in db [:contact-point :period :end]))]
     (-> db
         (update-in [:contact-point :period :start] (fn [] start))
         (update-in [:contact-point :period :end] (fn [] end))))))

(reg-event-db
 :set-contact-point-period-end
 validate-spec
 (fn [db [_ value]]
   (let [[start end] (check-period (get-in db [:contact-point :period :start]) value)]
     (-> db
         (update-in [:contact-point :period :start] (fn [] start))
         (update-in [:contact-point :period :end] (fn [] end))))))

(reg-event-db
 :add-contact-point
 validate-spec
 (fn [db [_]]
   (update-in db [:adding-patient :telecom] #(vec (conj % (:contact-point db))))))

(reg-event-db
 :remove-contact-point
 validate-spec
 (fn [db [_ i]]
   (update-in db [:adding-patient :telecom] #(utils/vec-remove % i))))


(reg-event-db
 :set-gender
 validate-spec
 (fn [db [_ value]]
   (update-in db [:adding-patient :gender] (fn [] value))))

(reg-event-db
 :set-birthdate
 validate-spec
 (fn [db [_ value]]
   (update-in db [:adding-patient :birthDate] (fn [] value))))
