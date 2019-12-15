(ns plzwork.views.spotlight
  (:require [re-frame.core :as rf]
            [plzwork.events :as evts]
            [reagent.core :as r]
            [plzwork.subs :as subs]))

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


(defn action [main opts shortcut]
  [:div.cmd [:span.main main] [:span.opts opts] [:span.shortcut shortcut]])

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
          [:div.spotlight
           {:position "relative"}
           [:input.cmd-txt {:placeholder "COMMAND"
                            :type "text"
                            :id "cmd-txt"
                            :auto-complete "off"
                            :auto-focus true
                            :value @value
                            :style {:display (if spotlight? "flex" "none")}
                            :on-click #(do (println "CLICK") (rf/dispatch-sync [::evts/open-spotlight]))
                            :on-change #(reset! value (-> % .-target .-value))}]
           [:ul.cmd-list {:style {:display (if spotlight? "grid" "none")}}
            [action "Open" "card" "o c"]
            [action "Send" "inbox | todos" "s [i t]"]
            [action "Query" "" "q"]
            [action "Reference" "card | page" "r [c p]"]]]))})))