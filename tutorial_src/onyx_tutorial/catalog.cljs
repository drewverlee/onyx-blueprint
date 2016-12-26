(ns onyx-tutorial.catalog
  (:require [cljs.pprint :as pprint]
            [goog.dom :as gdom]
            [onyx-blueprint.api :as api]
            [onyx-tutorial.builders :as b]))
; 

(def components
  [
   {:component/id ::graph-intro
    :component/type :blueprint/html
    :content/tag :p
    :content/text "Here we describe to onyx that were pulling data from Apache Kafka, doing a simple transformation  and putting the result in Postgres:"}

   {:component/id ::catalog-intro
    :component/type :blueprint/html
    :content/tag :p
    :content/text "Now we take this workflow and put it inside our catalog."}

   {:component/id ::user-catalog
    :component/type :blueprint/editor
    :evaluations/validate-spec :onyx.core/catalog
    :content/label "Catalog"
    :evaluations/init :content/catalog-input
    :content/default-input []} 

   {:component/id ::user-code-ex
    :component/type :blueprint/editor
    :evaluations/validate-spec :onyx.core/workflow
    :evaluations/init :content/default-input
    :content/default-input [
                            [:read-input-from-kafka :some-transofmration]
                            [:some-transofmration :write-output-to-postgres]]}


   {:component/id ::graph-ex
    :component/type :blueprint/graph
    :link/evaluations {:workflow ::user-code-ex}}




   {:component/id ::simulator-catalog
    :component/type :blueprint/editor
    :evaluations/init :content/default-input
    :evaluations/validate-spec :onyx.core/catalog
    :content/label "Catalog"
    :content/default-input [{:onyx/name :read-input
                             :onyx/type :input
                             :onyx/batch-size 1}
                            {:onyx/name :increment-n
                             :onyx/type :function
                             :onyx/fn :cljs.user/increment-n
                             :onyx/batch-size 1}
                            {:onyx/name :square-n
                             :onyx/type :function
                             :onyx/fn :cljs.user/square-n
                             :onyx/batch-size 1}
                            {:onyx/name :write-output
                             :onyx/type :output
                             :onyx/batch-size 1}]}


   (b/header ::title
             "Catalogs")

   (b/hiccup ::graph-desc
             [:div
              [:p "On Onyx, Catalogs are where you tell onyx about where the inputs for your task are coming from and where the outputs are going."]])])


(def sections [{:section/id :catalog-basics
                :section/layout [[::title]
                                 '[::graph-desc]
                                  [::graph-intro]
                                  [::user-code-ex]
                                 '[(::graph-ex {:graph-direction "LR"})]
                                  [::catalog-intro]
                                  [:simulator-catalog]]}])
                                 



(api/render-tutorial! components sections (gdom/getElement "app"))
