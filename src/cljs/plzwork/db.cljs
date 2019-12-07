(ns plzwork.db)

; (def default-db
;   {:name "re-frame"
;    :recent-tags []
;    :all-tags []
;    :recent-cards []
;    :card-meta {}})

(def default-db
  {:name "re-frame"
   :recent-tags ["programming/clojure" "databases/inmemory"]
   :all-tags ["programming/clojure" "databases/inmemory" "cats" "ideas/shit"]
   :recent-cards []
   :center-content :editor
   :spotlight? false
   :card-meta {:last-edited (.now js/Date)
               :tags        ["programming/swift" "nice"]
               :refs        {:to [{:id "SOMEID" :title "ASDASDASD" :tags ["abc/asd" "zxc/ccc"]}
                                  {:id "SOMEID2" :title "Database Systems" :tags ["cmu/db" "cs/db"]}]
                             :from [{:id "SOMEID" :title "ASDASDASD" :tags ["abc/asd" "zxc/ccc"]}]
                             :active :to}}})
