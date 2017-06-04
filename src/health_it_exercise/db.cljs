(ns health-it-exercise.db
  (:require [clojure.spec.alpha :as s]))

;; initial state of app-db
(def app-db {:client-id ""
             :client-secret ""

             :patients []

             :name {}
             :given-name ""
             :prefix-name ""
             :suffix-name ""
             :contact-point {}
             :birthdate ""
             :gender ""
             :adding-patient {:resourceType "Patient"}})
