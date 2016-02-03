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
    var gravity: CGFloat = -1200.0 // pixels per second
    let impulse: CGFloat = 400.0
    var playableStart: CGFloat = 0.0
    var numberOfFrames: Int = 4

    var jumpCount: Int = 0
    var kirbyTextures: Array<SKTexture> = []

    
    init(entity: GKEntity) {
        self.spriteComponent = entity.componentForClass(SpriteComponent)!
        for i in 0..<numberOfFrames {
            kirbyTextures.append(SKTexture(imageNamed: "Kirby\(i)"))
        }
    }
    
    func applyImpulse() {
        velocity = CGPoint(x: 0, y: impulse)
        animatePlayer()
    }
    
    func applyInitialImpulse() {
        velocity = CGPoint(x: 0, y: impulse * 1.5)
    }
    
    func animatePlayer() {
        if let player = entity as? Player {
            if jumpCount < 3 {
                jumpCount++
            } else {
                jumpCount = 0
            }
            player.spriteComponent.node.texture = kirbyTextures[jumpCount]
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
        spriteNode.position.x += vStep.x
        spriteNode.position.y += vStep.y
        
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