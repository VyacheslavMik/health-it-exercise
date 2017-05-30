(ns health-it-exercise.android.app-navigator
  (:require [reagent.core :as r]
            [health-it-exercise.android.login-screen :as login-screen]
            [health-it-exercise.android.patients-screen :as patients-screen]
            [health-it-exercise.android.patient-screen :as patient-screen]
            [health-it-exercise.android.add-patient-screen :as add-patient-screen]))

(def react-navigation (js/require "react-navigation"))
(def StackNavigator (aget react-navigation "StackNavigator"))

(def routes #js {:LoginScreen #js {:screen (r/reactify-component login-screen/login-screen)}
                 :PatientsScreen #js {:screen (r/reactify-component patients-screen/patients-screen)}
                 :PatientScreen #js {:screen (r/reactify-component patient-screen/patient-screen)}
                 :AddPatientScreen #js {:screen (r/reactify-component add-patient-screen/add-patient-screen)}})

(def routing (r/adapt-react-class (StackNavigator. routes #js {:initialRouteName "LoginScreen"
                                                               :headerMode "none"})))

(defn app-navigator []
  (fn []
    [routing]))
