(ns health-it-exercise.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :client-id
  (fn [db _]
    (:client-id db)))

(reg-sub
  :client-secret
  (fn [db _]
    (:client-secret db)))

(reg-sub
  :patients
  (fn [db _]
    (:patients db)))

(reg-sub
  :adding-patient
  (fn [db _]
    (:adding-patient db)))

(reg-sub
  :patient-names
  (fn [db _]
    (get-in db [:adding-patient :name])))

(reg-sub
  :name-use
  (fn [db _]
    (get-in db [:name :use])))

(reg-sub
  :given-names
  (fn [db _]
    (get-in db [:name :given])))

(reg-sub
  :prefix-names
  (fn [db _]
    (get-in db [:name :prefix])))

(reg-sub
  :suffix-names
  (fn [db _]
    (get-in db [:name :suffix])))

(reg-sub
  :name-period-start
  (fn [db _]
    (get-in db [:name :period :start])))

(reg-sub
  :name-period-end
  (fn [db _]
    (get-in db [:name :period :end])))

(reg-sub
  :patient-contact-points
  (fn [db _]
    (get-in db [:adding-patient :telecom])))

(reg-sub
  :contact-point-system
  (fn [db _]
    (get-in db [:contact-point :system])))

(reg-sub
  :contact-point-value
  (fn [db _]
    (get-in db [:contact-point :value])))

(reg-sub
  :contact-point-use
  (fn [db _]
    (get-in db [:contact-point :use])))

(reg-sub
  :contact-point-rank
  (fn [db _]
    (get-in db [:contact-point :rank])))

(reg-sub
  :contact-point-period-start
  (fn [db _]
    (get-in db [:contact-point :period :start])))

(reg-sub
  :contact-point-period-end
  (fn [db _]
    (get-in db [:contact-point :period :end])))

(reg-sub
  :patient-gender
  (fn [db _]
    (get-in db [:adding-patient :gender])))

(reg-sub
  :patient-birthdate
  (fn [db _]
    (get-in db [:adding-patient :birthDate])))
