//
//  PlayerEntity.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 26/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class Player: GKEntity {
    
    var spriteComponent: SpriteComponent!
    var movementComponent: MovementComponent!
    
    
    var movementAllowed: Bool = false
    
    init(imageName: String) {
        super.init()
        
        let texture: SKTexture = SKTexture(imageNamed: imageName)
        spriteComponent = SpriteComponent(entity: self, texture: texture, size: texture.size())
        addComponent(spriteComponent)
        
        movementComponent = MovementComponent(entity: self)
        addComponent(movementComponent)
        
        let spriteNode = spriteComponent.node
        
        spriteNode.physicsBody = SKPhysicsBody(rectangleOfSize: spriteNode.size)
        spriteNode.physicsBody?.categoryBitMask = PhysicsCategory.Player
        spriteNode.physicsBody?.collisionBitMask = 0
        spriteNode.physicsBody?.contactTestBitMask = PhysicsCategory.Obstacle | PhysicsCategory.Ground
    }
    
}