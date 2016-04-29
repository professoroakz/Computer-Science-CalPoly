package main

import (
    "encoding/json"
    "fmt"
    "net/http"
    "io/ioutil"
    "io"
    "bytes"
    "net/url"

    "github.com/gorilla/mux"
)

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Welcome!")
    fmt.Fprintln(w, "GET on /stocks/{key} returns value")
    fmt.Fprintln(w, "POST on /stocks with {\"key\":\"{key}\", \"value\":\"{value}\"}")
}

func GatewayStockShow(w http.ResponseWriter, r *http.Request){
    vars := mux.Vars(r)
    key := vars["key"]
    // Calculate node for forwarding
    // When read in file, use Map<Date, port> with first Date of each port
    // Use time/before for finding right port

    fmt.Sprintf("GET http://localhost:8081/stocks/" + key)
    resp, err := http.Get("http://localhost:8081/stocks/" + key)
    if err != nil {
        panic(fmt.Sprintf("ERROR IN GET"))
    }
    
    buf := new(bytes.Buffer)
    buf.ReadFrom(resp.Body)
    fmt.Fprintln(w, buf.String())
}

func GatewayStockCreate(w http.ResponseWriter, r *http.Request) {
     var stock Stock

    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    err = json.Unmarshal(body, &stock)

    // Calculate node for forwarding
    // When read in file, use Map<Date, port> with first Date of each port
    // Use time/before for finding right port
    resp, err := http.PostForm("http://localhost:8081/stocks/", url.Values{"key": {stock.Key}, "value": {stock.Value}})

    if err != nil {
        panic(fmt.Sprintf("ERROR IN POSt"))
    }
    
    buf := new(bytes.Buffer)
    buf.ReadFrom(resp.Body)
    fmt.Fprintln(w, buf.String())
}