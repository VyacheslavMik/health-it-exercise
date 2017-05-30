(ns health-it-exercise.android.logic
  (:require [health-it-exercise.android.api :as api]))

(defn load-patients [succ-fn err-fn]
  (api/load-patients #(succ-fn (vec (map (fn [resource] (:resource resource)) %)))
                     err-fn))
