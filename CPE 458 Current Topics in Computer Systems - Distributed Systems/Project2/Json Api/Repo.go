package main

// import "fmt"

var stocks Stocks

// Give us some seed data
func init() {
    RepoCreateStock(Stock{"4-18-16", "60834000,107.480003"})
    RepoCreateStock(Stock{"4-15-16", "46418500,109.849998"})
}

func RepoFindStock(key string) Stock {
    for _, s := range stocks {
        if s.Key == key {
            return s
        }
    }
    // return empty Stock if not found
    return Stock{}
}

func RepoCreateStock(s Stock) Stock {
    // s := Stock{Key: key, Value: value}
    stocks = append(stocks, s)
    return s
}

// func RepoDestroyTodo(id int) error {
//     for i, t := range todos {
//         if t.Id == id {
//             todos = append(todos[:i], todos[i+1:]...)
//             return nil
//         }
//     }
//     return fmt.Errorf("Could not find Todo with id of %d to delete", id)
// }