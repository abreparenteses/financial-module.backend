(ns unit.financial-module.adapters-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [financial-module.adapters :as adapters]
            [financial-module.schemas.db :as schemas.db]
            [financial-module.schemas.types :as schemas.types]
            [financial-module.schemas.wire-in :as schemas.wire-in]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest inst->formated-string
  (testing "should adapt clojure/instant to formated string"
    (is (= "1987-02-10 09:38:43"
           (adapters/inst->utc-formated-string #inst "1987-02-10T09:38:43.000Z"
                                               "yyyy-MM-dd hh:mm:ss")))))
(deftest db->wire-in
  (testing "should adapt db/accounts-payable-entry to wire-in/accounts-payable-entry"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          amount 10M
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:accounts_payable{:id id
                                      :name name
                                      :description description
                                      :amount amount
                                      :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :amount (bigdec amount)
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters/db->wire-in db-entry))))))
