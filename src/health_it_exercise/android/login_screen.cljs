(ns health-it-exercise.android.login-screen
  (:require [re-frame.core :refer [subscribe dispatch]]
            [health-it-exercise.subs]
            [health-it-exercise.events]
            [health-it-exercise.android.ui :as ui]
            [health-it-exercise.android.logic :as logic]
            [health-it-exercise.android.api :as api]
            [health-it-exercise.android.patients-screen :as patients-screen]))

(defn load-patients [navigate]
  (logic/load-patients
   (fn [patients]
     (doseq [patient patients]
       (dispatch [:add-patient patient]))
     (navigate "PatientsScreen"))
   
   (fn [] (ui/alert "Loading" "Patients loading error"))))

(defn connect [navigate]
  (api/login @(subscribe [:client-id])
             @(subscribe [:client-secret])
             #(load-patients navigate)
             #(ui/alert "Login" "Login failed")))

(defn login-screen [props]
  (let [{:keys [navigation]} props
        navigate (.-navigate navigation)]
    (fn []
      [ui/view
       [ui/text "Login"]
       [ui/input {:on-change-text #(dispatch [:set-client-id %])}]
       [ui/text "Password"]
       [ui/input {:on-change-text #(dispatch [:set-client-secret %])}]
       [ui/button {:title "Connect" :on-press #(connect navigate)}]])))
