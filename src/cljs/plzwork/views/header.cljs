(ns plzwork.views.header
  (:require [re-frame.core :as rf]
            [plzwork.events :as evts]
            [plzwork.subs :as subs]))

(defn action [main opts shortcut]
  [:div.cmd [:span.main main] [:span.opts opts] [:span.shortcut shortcut]])

(defn tgl-btn []
  [:div
   [:input#cb3.tgl.tgl-skewed {:type "checkbox" :on-change (rf/dispatch [::evts/toggle-ref-list])}]
   [:label.tgl-btn
    {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn spotlight []
  (let [spotlight? (rf/subscribe [::subs/spotlight?])]
    [:div.cmd-bar
     [:input.cmd-txt {:placeholder "COMMAND" :id "cmd-txt"}]
     [:ul.cmd-list {:style {:display (when (not @spotlight?) "none")}}
      [action "Open" "card" "o c"]
      [action "Send" "inbox | todos" "s [i t]"]
      [action "Query" "" "q"]
      [action "Reference" "card | page" "r [c p]"]]]))

(defn ref-lbl []
  [:div.ref-lbl
  ;  [:div {:style {:transition "all 0.4s ease" :background "black" :transform (str "skew(-12deg) " (when @from? "rotateX(180deg)")) :border-radius "2px"  :width "8px" :height "2em"}}]
   [:div {:style {:transition "all 0.4s ease" :background "black" :transform (str "skew(-12deg)") :border-radius "2px"  :width "8px" :height "2em"}}]
   [:h2 {:style {:margin "5px" :padding-bottom 2 :padding-right "5px"}} "REFS"]
   [tgl-btn]])

(defn header []
  [:div.header
   [ref-lbl]
   [spotlight]
   [:div.recent-lbl]])