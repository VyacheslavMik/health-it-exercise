(ns health-it-exercise.android.patients-screen
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe]]
            [health-it-exercise.android.utils :as utils]
            [health-it-exercise.android.ui :as ui]))

(def ds (ui/ReactNative.ListView.DataSource. #js{:rowHasChanged (fn[a b] false)}))

(def row-comp (r/reactify-component
               (fn [props]
                 (let [row (props :row)
                       navigate (props :navigate)
                       patient (js->clj @row :keywordize-keys true)]
                   [ui/touchable-highlight
                    {:style {:border-top-width 1 :border-color "#000"}
                     :on-press #(navigate "PatientScreen" {:patient patient})}
                    [ui/text (utils/patient->string patient)]]))))

(defn patients-screen [props]
  (let [{:keys [navigation]} props
        navigate (.-navigate navigation)]
    (fn []
      (let [rows (subscribe [:patients])]
        [ui/view
         [ui/button {:title "Add Patient" :on-press #(navigate "AddPatientScreen")}]
         [ui/list-view {:dataSource (.cloneWithRows ds (clj->js @rows))
                        :render-row (fn [row] (r/create-element row-comp #js{:row row :navigate navigate}))}]]))))
