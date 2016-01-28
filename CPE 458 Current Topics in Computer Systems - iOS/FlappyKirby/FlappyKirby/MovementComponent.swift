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
    var jumpCount = 0
    let kirby: [String] = ["Kirby0", "Kirby1", "Kirby2", "Kirby3"]
    var playableStart: CGFloat = 0
    
    
    init(entity: GKEntity) {
        self.spriteComponent = entity.componentForClass(SpriteComponent)!
    }
    
    func applyImpulse() {
        velocity = CGPoint(x: 0, y: impulse)
        animatePlayer()
    }
    
    func animatePlayer() {
        if let player = entity as? Player {
            if jumpCount <= 3 {
                player.spriteComponent.node.texture = SKTexture(imageNamed: kirby[jumpCount])
                jumpCount++
            } else {
                jumpCount = 0
                player.spriteComponent.node.texture = SKTexture(imageNamed: kirby[jumpCount])
            }
        }
    }
    
    func applyMovement(seconds: NSTimeInterval) {
        let spriteNode = spriteComponent.node
        
        // Apply Gravity
        let gStep = CGPoint(x: 0, y: gravity * CGFloat(seconds))
        velocity.x += gStep.x
        velocity.y += gStep.y
        
        
        // Apply velocity
        let vStep = CGPoint(x: velocity.x * CGFloat(seconds), y: velocity.y * CGFloat(seconds))
        spriteNode.position += vStep
        
        // Temporary ground hit
        if spriteNode.position.y - spriteNode.size.height/2 < playableStart {
            spriteNode.position = CGPoint(x: spriteNode.position.x, y: playableStart + spriteNode.size.height/2)
        }
    }

    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        if let player = entity as? Player {
            if player.movementAllowed {
                applyMovement(seconds)
            }
        }
    }
    
}