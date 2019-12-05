(ns plzwork.views.header
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [plzwork.events :as evts]
            [plzwork.subs :as subs]))

(defn action [main opts shortcut]
  [:div.cmd [:span.main main] [:span.opts opts] [:span.shortcut shortcut]])

(defn tgl-btn []
  [:div
   [:input#cb3.tgl.tgl-skewed {:type "checkbox" :on-change  #(rf/dispatch [::evts/toggle-ref-list])}]
   [:label.tgl-btn
    {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn handle [type arg]
  (let [cmd  (case type
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
                                                                (reset! value "")
                                                                (parse-and-dispatch @value)))))
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
  ;  [:div {:style {:transition "all 0.4s ease" :background "black" :transform (str "skew(-12deg) " (when @from? "rotateX(180deg)")) :border-radius "2px"  :width "8px" :height "2em"}}]
   [:div {:style {:transition "all 0.4s ease" :background "black" :transform (str "skew(-12deg)") :border-radius "2px"  :width "8px" :height "2em"}}]
   [:h2 {:style {:margin "5px" :padding-bottom 2 :padding-right "5px"}} "REFS"]
   [tgl-btn]])

(defn header []
  [:div.header
   [ref-lbl]
   [spotlight]
   [:div.recent-lbl]])