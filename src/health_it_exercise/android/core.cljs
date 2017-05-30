(ns health-it-exercise.android.core
  (:require [reagent.core :as r]
            [health-it-exercise.android.ui :as ui]
            [health-it-exercise.android.app-navigator :as app-navigator]))

(def app-registry (.-AppRegistry ui/ReactNative))

(defn app-root []
  (let []
    (fn []
      [app-navigator/app-navigator])))

(defn init []
  (.registerComponent app-registry "HealthItExercise" #(r/reactify-component app-root)))
