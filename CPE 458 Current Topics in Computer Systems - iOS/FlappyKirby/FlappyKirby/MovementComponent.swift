//
//  MovementComponent.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 26/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameKit

class MovementComponent: GKComponent {
    let spriteComponent: SpriteComponent
    
    var velocity = CGPoint.zero    
    let gravity: CGFloat = -1200.0 // pixels per second
    let impulse: CGFloat = 400.0
    
    var playableStart: CGFloat = 0
    
    
    init(entity: GKEntity) {
        self.spriteComponent = entity.componentForClass(SpriteComponent)!
    }
    
    func applyImpulse() {
        velocity = CGPoint(x: 0, y: impulse)
    }
    
    func applyMovement(seconds: NSTimeInterval) {
        let spriteNode = spriteComponent.node
        
        // Apply gravity
        //let gravityStep = CGPoint(x: 0, y: gravity) * CGFloat(seconds)
       // velocity += gravityStep
        
        let gStep = CGPoint(x: 0, y: gravity * CGFloat(seconds))
        velocity.x += gStep.x
        velocity.y += gStep.y
        
        
        // Apply velocity
        let velocityStep = velocity * CGFloat(seconds)
        spriteNode.position += velocityStep
        
        // Temporary ground hit
        if spriteNode.position.y - spriteNode.size.height/2 < playableStart {
            spriteNode.position = CGPoint(x: spriteNode.position.x, y: playableStart + spriteNode.size.height/2)
        }
    }

    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        applyMovement(seconds)
    }
    
}