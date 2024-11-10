(ns financial-module.schemas.wire-in
  (:require [schema.core :as s]))

(s/defschema AccountsPayableId
  {:id s/Uuid})

(s/defschema AccountsPayableUpsertEntry
  {:name s/Str
   :description s/Str
   :amount s/Num})

(s/defschema AccountsPayableEntry
  {:id s/Uuid
   :name s/Str
   :description s/Str
   :amount s/Num
   :created-at s/Inst})

(s/defschema AccountsPayableHistory
  {:entries [AccountsPayableEntry]
   :total s/Num})
