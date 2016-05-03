package main

import (
    "encoding/json"
    "fmt"
    "net/http"
    "io/ioutil"
    "io"

    "github.com/gorilla/mux"
)

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Welcome!")
    fmt.Fprintln(w, "GET on /stocks/{key} returns value")
    fmt.Fprintln(w, "POST on /stocks with {\"key\":\"{key}\", \"value\":\"{value}\"}")
}

func StockShow(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    key := vars["key"]
    s := RepoFindStock(key)
    fmt.Fprintln(w, s.Value)
}

func StockCreate(w http.ResponseWriter, r *http.Request) {
    var stock Stock
    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    if err := json.Unmarshal(body, &stock); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    }

    s := RepoCreateStock(stock) // <-----
    
    w.Header().Set("Content-Type", "application/json; charset=UTF-8")
    w.WriteHeader(http.StatusCreated)
    if err := json.NewEncoder(w).Encode(s); err != nil {
        panic(err)
    }
}