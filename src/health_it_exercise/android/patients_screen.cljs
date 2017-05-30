(ns health-it-exercise.android.patients-screen
  (:require [reagent.core :as r :refer [atom]]
            [health-it-exercise.android.utils :as utils]
            [health-it-exercise.android.ui :as ui]))

(def ds (ui/ReactNative.ListView.DataSource. #js{:rowHasChanged (fn[a b] false)}))

(defn list-view-source [v]
  (let [res #js[]]
    (doseq [item v]
      (.push res (r/atom item)))
    (r/atom (js->clj res))))

(def rows nil)

(defn add-row [patient]
  (swap! rows conj (r/atom patient)))

(defn set-patients [patients]
  (set! rows (list-view-source (clj->js patients))))

(def row-comp (r/reactify-component
               (fn [props]
                 (let [row (props :row)
                       navigate (props :navigate)
                       patient (js->clj @row :keywordize-keys true)]
                   [ui/touchable-highlight {:style {:border-top-width 1 :border-color "#000"} :on-press #(navigate "PatientScreen" {:patient patient})}
                    [ui/text (utils/patient->string patient)]]))))

(defn patients-screen [props]
  (let [{:keys [navigation]} props
        navigate (.-navigate navigation)]
    (fn []
      [ui/view
       [ui/button {:title "Add Patient" :on-press #(navigate "AddPatientScreen")}]
       [ui/list-view {:dataSource (.cloneWithRows ds (clj->js @rows))
                      :render-row (fn [row] (r/create-element row-comp #js{:row row :navigate navigate}))}]])))
