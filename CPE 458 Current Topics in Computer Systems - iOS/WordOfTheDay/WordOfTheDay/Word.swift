//
//  Word.swift
//  WordOfTheDay
//
//  Created by Oktay Gardener on 01/02/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import Foundation

class Word {
    let id: Int
    let permalink: String
    let word: String
    let definition: String
    let description: String
    let date: NSDate?
    
    init(id: Int, permalink: String, word: String, definition: String, description: String) {
        self.id = id
        self.permalink = permalink
        self.word = word
        self.definition = definition
        self.description = description
        self.date = NSDate()
    }
    
    init(dictionary: [String : AnyObject]) {
        // Note: This is a naive implementation of JSON parsing.
        // We might use Decodable: https://github.com/Anviking/Decodable
        
        id = dictionary["id"] as! Int
        permalink = dictionary["permalink"] as! String
        word = dictionary["word"] as! String
        definition = dictionary["definition"] as! String
        description = dictionary["description"] as! String
        
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let dateString = dictionary["sale_starts"] as! String
        date = dateFormatter.dateFromString(dateString)!
    }
}
