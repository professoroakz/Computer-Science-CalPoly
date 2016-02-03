//
//  WordOfTheDay.swift
//  WordOfTheDay
//
//  Created by Oktay Gardener on 01/02/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import Foundation

class WordOfTheDay {
    static let sharedInstance = WordOfTheDay()
    static let requestedNewWordNotificationName = "wotd.newword.wordoftheday.notification"
    static let requestedNewSlangWordNotification = "wotd.newslangword.wordoftheday.notification"
    
    var word: [Word] = [] {
        didSet {
            postRequestedNewWordNotification()
        }
    }
    
    var slangWord: [SlangWord] = [] {
        didSet {
            postRequestedNewWordNotification()
        }
    }
    
    init() {
        
    }
    
    init(word: [Word], slangWord: [SlangWord]) {
        
    }
    
    private func postRequestedNewWordNotification() {
        NSNotificationCenter.defaultCenter().postNotificationName(WordOfTheDay.requestedNewWordNotificationName, object: self)
    }
    
    private func postRequestedNewSlangWordNotification() {
        NSNotificationCenter.defaultCenter().postNotificationName(WordOfTheDay.requestedNewSlangWordNotification, object: self)
    }
    
}