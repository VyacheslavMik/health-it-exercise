(ns health-it-exercise.android.add-patient-screen
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [goog.date.Date]
            [health-it-exercise.android.ui :as ui]
            [health-it-exercise.android.utils :as utils]
            [health-it-exercise.android.patients-screen :as patients-screen]
            [health-it-exercise.android.api :as api]))

(defn add-patient [go-back]
  (let [patient @(subscribe [:adding-patient])]
    (api/create-patient patient
                        #(do
                           (dispatch [:add-patient patient])
                           (go-back nil))
                        #(ui/alert "Add patient" "Add patient error"))))

(defn patient-names [parent]
  (let [names (subscribe [:patient-names])]
    (for [[i name] (map-indexed #(vector %1 %2) @names)]
      ^{:key i}
      [ui/view
       (ui/name-info name)
       [ui/button {:title "Remove Name" :on-press #(dispatch [:remove-name i])}]])))

(defn given-names [parent]
  (let [given (subscribe [:given-names])]
    (for [[i item] (map-indexed #(vector %1 %2) @given)]
      ^{:key i}
      [ui/view
       [ui/text (str item)]
       [ui/button {:title "Remove given name" :on-press #(dispatch [:remove-given-name i])}]])))

(defn prefix-names [parent]
  (let [prefix (subscribe [:prefix-names])]
    (for [[i item] (map-indexed #(vector %1 %2) @prefix)]
      ^{:key i}
      [ui/view
       [ui/text (str item)]
       [ui/button {:title "Remove prefix" :on-press #(dispatch [:remove-prefix-name i])}]])))

(defn suffix-names [parent]
  (let [suffix (subscribe [:suffix-names])]
    (for [[i item] (map-indexed #(vector %1 %2) @suffix)]
      ^{:key i}
      [ui/view
       [ui/text (str item)]
       [ui/button {:title "Remove suffix" :on-press #(dispatch [:remove-suffix-name i])}]])))

(defn patient-contact-points [parent]
  (let [contact-points (subscribe [:patient-contact-points])]
    (for [[i point] (map-indexed #(vector %1 %2) @contact-points)]
      ^{:key i}
      [ui/view
       (ui/cp-info point)
       [ui/button {:title "Remove Contact Point" :on-press #(dispatch [:remove-contact-point i])}]])))

(defn add-patient-screen [props]
  (let [{:keys [navigation]} props
        go-back (.-goBack navigation)
        this (r/current-component)]
    (fn []
      [ui/view
       [ui/scroll-view
        [ui/button {:title "Go Back" :on-press #(go-back nil)}]
        [ui/text {:style {:height 10}}]
        [ui/button {:title "Add" :on-press #(add-patient go-back)}]

        [ui/text {:style {:font-size 20 :font-weight "bold"}} "Names"]
        (patient-names this)
        
        [ui/text {:style {:margin-top 20 :font-size 20 :font-weight "bold"}} "New name properties"]
        [ui/text "Purpose of this name"]
        [ui/picker {:selected-value @(subscribe [:name-use]) :on-value-change #(dispatch [:set-name-use %])}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Usual" :value "usual"}]
         [ui/picker-item {:label "Official" :value "official"}]
         [ui/picker-item {:label "Temporary" :value "temp"}]
         [ui/picker-item {:label "Nickname" :value "nickname"}]
         [ui/picker-item {:label "Anonymous" :value "anonymous"}]
         [ui/picker-item {:label "Old" :value "old"}]
         [ui/picker-item {:label "Maiden" :value "maiden"}]]
        
        [ui/text "Text representation of the name"]
        [ui/input {:on-change-text #(dispatch [:set-name-text %])}]
        
        [ui/text "Family name (often called 'Surname')"]
        [ui/input {:on-change-text #(dispatch [:set-name-family %])}]

        [ui/text "Given names"]
        (given-names this)

        [ui/text "Given name"]
        [ui/input {:on-change-text #(dispatch [:set-given-name %])}]
        [ui/button {:title "Add given name" :on-press #(dispatch [:add-given-name])}]
        
        [ui/text "Parts that come before the name"]
        (prefix-names this)

        [ui/text "Prefix"]
        [ui/input {:on-change-text #(dispatch [:set-prefix-name %])}]
        [ui/button {:title "Add prefix" :on-press #(dispatch [:add-prefix-name])}]
        
        [ui/text "Parts that come after the name"]
        (suffix-names this)

        [ui/text "Suffix"]
        [ui/input {:on-change-text #(dispatch [:set-suffix-name %])}]
        [ui/button {:title "Add suffix" :on-press #(dispatch [:add-suffix-name])}]

        [ui/text "Period Start"]
        [ui/date-picker {:placeholder "Select Date"
                         :date @(subscribe [:name-period-start])
                         :on-date-change #(dispatch [:set-name-period-start %])}]
        [ui/button {:title "Clear Date" :on-press #(dispatch [:set-name-period-start ""])}]

        [ui/text "Period End"]
        [ui/date-picker {:placeholder "Select Date"
                         :date @(subscribe [:name-period-end])
                         :on-date-change #(dispatch [:set-name-period-end %])}]
        [ui/button {:title "Clear Date" :on-press #(dispatch [:set-name-period-end ""])}]

        [ui/button {:title "Add Name" :on-press #(dispatch [:add-name])}]

        [ui/text {:style {:font-size 20 :font-weight "bold"}} "Contact Points"]
        (patient-contact-points this)

        [ui/text {:style {:margin-top 20 :font-size 20 :font-weight "bold"}} "New contact properties"]
        [ui/text "System"]
        [ui/picker {:selected-value @(subscribe [:contact-point-system])
                    :on-value-change #(dispatch [:set-contact-point-system %])}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Phone" :value "phone"}]
         [ui/picker-item {:label "Fax" :value "fax"}]
         [ui/picker-item {:label "Email" :value "email"}]
         [ui/picker-item {:label "Pager" :value "pager"}]
         [ui/picker-item {:label "URL" :value "url"}]
         [ui/picker-item {:label "SMS" :value "sms"}]
         [ui/picker-item {:label "other" :value "other"}]]
        
        [ui/text "Value"]
        [ui/input {:on-change-text #(dispatch [:set-contact-point-value %])}]
        
        [ui/text "Use"]
        [ui/picker {:selected-value @(subscribe [:contact-point-use])
                    :on-value-change #(dispatch [:set-contact-point-use %])}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Home" :value "home"}]
         [ui/picker-item {:label "Work" :value "work"}]
         [ui/picker-item {:label "Temp" :value "temp"}]
         [ui/picker-item {:label "Old" :value "old"}]
         [ui/picker-item {:label "mobile" :value "mobile"}]]
        
        [ui/text "Rank"]
        [ui/input {:on-change-text #(dispatch [:set-contact-point-rank %])
                   :value (str @(subscribe [:contact-point-rank]))}]

        [ui/text "Period Start"]
        [ui/date-picker {:placeholder "Select Date"
                         :date @(subscribe [:contact-point-period-start])
                         :on-date-change #(dispatch [:set-contact-point-period-start %])}]
        [ui/button {:title "Clear Date" :on-press #(dispatch [:set-contact-point-period-start ""])}]

        [ui/text "Period End"]
        [ui/date-picker {:placeholder "Select Date"
                         :date @(subscribe [:contact-point-period-end])
                         :on-date-change #(dispatch [:set-contact-point-period-end %])}]
        [ui/button {:title "Clear Date" :on-press #(dispatch [:set-contact-point-period-end ""])}]

        [ui/button {:title "Add Contact Point" :on-press #(dispatch [:add-contact-point])}]

        [ui/text "Gender"]
        [ui/picker {:selected-value @(subscribe [:patient-gender])
                    :on-value-change #(dispatch [:set-gender %])}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Male" :value "male"}]
         [ui/picker-item {:label "Female" :value "female"}]
         [ui/picker-item {:label "Other" :value "other"}]
         [ui/picker-item {:label "Unknown" :value "unknown"}]]

        [ui/text "Birthdate"]
        [ui/date-picker {:placeholder "Select Date"
                         :date @(subscribe [:patient-birthdate])
                         :on-date-change #(dispatch [:set-birthdate %])}]
        [ui/button {:title "Clear Date" :on-press #(dispatch [:set-birthdate %])}]]])))
