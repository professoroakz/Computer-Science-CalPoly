//
//  FavoriteWordsModel.swift
//  WordOfTheDay
//
//  Created by Oktay Gardener on 01/02/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

// Populate favorite words that the user has

import Foundation

class FavoriteWords {
    static let sharedInstance = FavoriteWords()
    
    var favoriteRegularWords: [Word] = []
    var favoriteSlangWords: [SlangWord] = []
    var favoriteWords: [Any] = []
    
    init() {
        
    }
    
}

extension Word {
    var isFavorited: Bool {
        get {
            return false
        }
        set {
            return
        }
    }
}

extension SlangWord {
    var isFavorited: Bool {
        get {
            return false
        }
        set {
            return
        }
    }
}