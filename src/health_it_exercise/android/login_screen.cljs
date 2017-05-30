(ns health-it-exercise.android.login-screen
  (:require [health-it-exercise.android.ui :as ui]
            [health-it-exercise.android.logic :as logic]
            [health-it-exercise.android.api :as api]
            [health-it-exercise.android.patients-screen :as patients-screen]))

(def login "test")
(def password "123")

(defn login-screen [props]
  (let [{:keys [navigation]} props
        navigate (.-navigate navigation)]
    (fn []
      [ui/view
       [ui/text "Login"]
       [ui/input {:value login :on-change-text (fn [text] (set! login text))}]
       [ui/text "Password"]
       [ui/input {:value password :on-change-text (fn [text] (set! password text))}]
       [ui/button {:title "Connect" :on-press (fn [] (api/login login
                                                                password
                                                                #(do
                                                                   (logic/load-patients (fn [patients]
                                                                                          (patients-screen/set-patients patients)
                                                                                          (navigate "PatientsScreen"))
                                                                                        (fn [] (ui/alert "Loading" "Patients loading error"))))
                                                                #(ui/alert "Login" "Login failed")))}]])))
