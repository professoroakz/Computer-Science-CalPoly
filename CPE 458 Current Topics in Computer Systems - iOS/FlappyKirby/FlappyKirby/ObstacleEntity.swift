//
//  ObstacleEntity.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 26/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import Foundation
import SpriteKit
import GameplayKit

class Obstacle: GKEntity {
    var spriteComponent: SpriteComponent!
    
    init(imageName: String) {
        super.init()
        let texture = SKTexture(imageNamed: imageName)
        spriteComponent = SpriteComponent(entity: self, texture: texture, size: texture.size())
        addComponent(spriteComponent)
        
        let spriteNode = spriteComponent.node
        
        spriteNode.physicsBody = SKPhysicsBody(rectangleOfSize: spriteNode.size)
        spriteNode.physicsBody?.categoryBitMask = PhysicsCategory.Obstacle
        spriteNode.physicsBody?.collisionBitMask = 0
        spriteNode.physicsBody?.contactTestBitMask = PhysicsCategory.Player
    }
}