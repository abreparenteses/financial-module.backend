(ns financial-module.db
  (:require [financial-module.schemas.db :as schemas.db]
            [financial-module.schemas.types :as schemas.types]
            [honey.sql :as sql]
            [honey.sql.helpers :as sql.helpers]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.core :as s]))

(s/defn insert-accounts-payable-transaction
  [transaction :- schemas.db/AccountsPayableTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/insert-into :accounts_payable)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(s/defn update-accounts-payable-entry-transaction
  [{:accounts_payable/keys [id name description amount]} :- schemas.db/AccountsPayableTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :accounts_payable)
           (sql.helpers/set {:name name
                             :description description
                             :amount amount})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)
       first))

(s/defn remove-accounts-payable-entry
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :accounts_payable)
           (sql.helpers/set {:removed true})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)))

(s/defn get-accounts-payable-all-transactions :- [schemas.db/AccountsPayableEntry]
  [db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :amount :created_at)
       (sql.helpers/from :accounts_payable)
       (sql.helpers/where [:= :removed false])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-accounts-payable-by-id :- [schemas.db/AccountsPayableEntry]
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :amount :created_at)
       (sql.helpers/from :accounts_payable)
       (sql.helpers/where [:= :removed false]
                          [:= :id id])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-accounts-payable-total :- s/Num
  [db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/select :%sum.amount)
           (sql.helpers/from :accounts_payable)
           sql/format)
       (components.database/execute db)
       first
       :sum))
