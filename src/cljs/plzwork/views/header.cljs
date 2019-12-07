(ns plzwork.views.header
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [plzwork.events :as evts]
            [plzwork.subs :as subs]))

(defn action [main opts shortcut]
  [:div.cmd [:span.main main] [:span.opts opts] [:span.shortcut shortcut]])

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

(defn handle [t arg]
  (println "CALLED WITH " t arg)
  (let [cmd  (case t
               :open (case arg
                       ("i" "inbox") :open-inbox
                       ("t" "todos") :open-todos
                       :open-cards)
               :send (case arg
                       ("i" "inbox") :send->inbox
                       ("t" "todo" "todos") :send->todos)
               :ref (case arg
                      ("p" "page") :ref-page
                      :open-ref-dialog))]
    (rf/dispatch [::evts/cmd-invoked {:id cmd}])))


(defn parse-and-dispatch [v]
  (let [cmd (clojure.string/split v " ")]
   (-> (case (first cmd)
         ("o" "open") :open
         ("s" "send") :send
         ("r" "ref" "reference") :ref
         nil)
       (handle (second cmd)))))



(defn spotlight []
  (let [value (r/atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [_] (.addEventListener (.getElementById js/document "cmd-txt")
                                 "keyup"
                                 #(when (= (.-key %) "Enter") (do 
                                                                (rf/dispatch [::evts/close-spotlight])
                                                                (parse-and-dispatch @value)
                                                                (reset! value "")))))
      :reagent-render
      (fn []
        (let [spotlight? @(rf/subscribe [::subs/spotlight?])]
          [:div.cmd-bar
           [:input.cmd-txt {:placeholder "COMMAND"
                            :type "text"
                            :id "cmd-txt"
                            :value @value
                            :on-click #(do (println "CLICK") (rf/dispatch-sync [::evts/open-spotlight]))
                            :on-change #(reset! value (-> % .-target .-value))}]
           [:ul.cmd-list {:style {:display (if (not spotlight?) "none" "block")}}
            [action "Open" "card" "o c"]
            [action "Send" "inbox | todos" "s [i t]"]
            [action "Query" "" "q"]
            [action "Reference" "card | page" "r [c p]"]]]))})))

(defn ref-lbl []
  [:div.ref-lbl
   [:h2 "References"]
   [arrow]])

(defn header []
  [:div.header
   [ref-lbl]
   [spotlight]
   [:div.recent-lbl]])