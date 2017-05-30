(ns health-it-exercise.android.patient-screen
  (:require [reagent.core :as r]
            [health-it-exercise.android.ui :as ui]))

(defn patient-screen [props]
  (let [{:keys [navigation]} props
        go-back (.-goBack navigation)
        patient (:patient (aget navigation "state" "params"))
        this (r/current-component)]
    (fn []
      [ui/view
       [ui/button {:title "Go Back" :on-press #(go-back nil)}]
       [ui/text {:style {:font-size 20 :font-weight "bold"}} "Names"]
       (for [[i name] (map-indexed #(vector %1 %2) (:name patient))]
         ^{:key i}
         [ui/view
          (ui/name-info name)])
       [ui/text {:style {:font-size 20 :font-weight "bold"}} "Contact Points"]
       (for [[i point] (map-indexed #(vector %1 %2) (:telecom patient))]
         ^{:key i}
         [ui/view
          (ui/cp-info point)])
       [ui/text (str "Gender: " (:gender patient))]
       [ui/text (str "Birthdate: " (:birthDate patient))]])))
