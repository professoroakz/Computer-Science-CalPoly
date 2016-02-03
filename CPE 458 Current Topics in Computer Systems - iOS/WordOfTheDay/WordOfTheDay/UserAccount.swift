//
//  UserAccount.swift
//  WordOfTheDay
//
//  Created by Oktay Gardener on 01/02/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import Foundation

class UserAccount {
    static let sharedInstance = UserAccount()
    
    static let successfulSignInNotificationName = "wotd.successful.signin.notification"
    static let unsuccessfulSignInNotificationName = "wotd.unsuccessful.signin.notification"
    
    
    init() {
        
    }
    
    
}