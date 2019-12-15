(ns plzwork.views.header
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [plzwork.events :as evts]
            [plzwork.subs :as subs]))



; (defn tgl-btn []
;   [:div
;    [:input#cb3.tgl.tgl-skewed {:type "checkbox" :on-change  #(rf/dispatch [::evts/toggle-ref-list])}]
;    [:label.tgl-btn
;     {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn arrow []
  [:svg
   {:fill "none", :height "26", :width "25"}
   [:path
    {:fill "#000"
     :d
     "M6.607 20.328a1 1 0 001.16.81l8.862-1.563a1 1 0 10-.348-1.97l-7.878 1.39-1.39-7.878a1 1 0 10-1.97.348l1.564 8.863zm11.07-16.325L6.773 19.581l1.639 1.146L19.316 5.15l-1.639-1.147z"}]])

(defn tgl-btn []
  [:div
   [:input#cb3 {:type "checkbox" :on-change  #(rf/dispatch [::evts/toggle-ref-list])}]
   [:label
    {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn title []
  (let [value (r/atom nil)]
    (fn []
      [:input#title-input {:placeholder "TITLE" :value @value :on-change #(reset! value (-> % .-target .-value))}])))

(defn ref-lbl []
  [:div.ref-lbl {:on-click #(rf/dispatch [::evts/toggle-ref-list])}
   [:button.texty-btn "References"]
   [arrow]])

(defn recent-lbl []
  [:div.recent-lbl
   [:button.texty-btn "Tags"]])

(defn header []
  [:div.header
   [ref-lbl]
   [title]
   [recent-lbl]])