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

    init(imageName: String) {
        super.init()
        
        let texture: SKTexture = SKTexture(imageNamed: imageName)
        spriteComponent = SpriteComponent(entity: self, texture: texture, size: texture.size())
        addComponent(spriteComponent)
        
        movementComponent = MovementComponent(entity: self)
        addComponent(movementComponent)
        
        let spriteNode = spriteComponent.node
        let offsetX = spriteNode.frame.size.width * spriteNode.anchorPoint.x
        let offsetY = spriteNode.frame.size.height * spriteNode.anchorPoint.y
        
        let path = CGPathCreateMutable()
        
        CGPathMoveToPoint(path, nil, 10 - offsetX, 5 - offsetY) 
        CGPathAddLineToPoint(path, nil, 8 - offsetX, 1 - offsetY) 
        CGPathAddLineToPoint(path, nil, 4 - offsetX, 1 - offsetY) 
        CGPathAddLineToPoint(path, nil, 1 - offsetX, 0 - offsetY) 
        CGPathAddLineToPoint(path, nil, 0 - offsetX, 2 - offsetY) 
        CGPathAddLineToPoint(path, nil, 0 - offsetX, 7 - offsetY) 
        CGPathAddLineToPoint(path, nil, 1 - offsetX, 10 - offsetY) 
        CGPathAddLineToPoint(path, nil, 3 - offsetX, 16 - offsetY) 
        CGPathAddLineToPoint(path, nil, 2 - offsetX, 23 - offsetY) 
        CGPathAddLineToPoint(path, nil, 0 - offsetX, 30 - offsetY) 
        CGPathAddLineToPoint(path, nil, 5 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 9 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 13 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 18 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 20 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 22 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 26 - offsetX, 31 - offsetY) 
        CGPathAddLineToPoint(path, nil, 27 - offsetX, 29 - offsetY) 
        CGPathAddLineToPoint(path, nil, 29 - offsetX, 26 - offsetY) 
        CGPathAddLineToPoint(path, nil, 28 - offsetX, 24 - offsetY) 
        CGPathAddLineToPoint(path, nil, 28 - offsetX, 22 - offsetY) 
        CGPathAddLineToPoint(path, nil, 28 - offsetX, 19 - offsetY) 
        CGPathAddLineToPoint(path, nil, 28 - offsetX, 16 - offsetY) 
        CGPathAddLineToPoint(path, nil, 27 - offsetX, 12 - offsetY) 
        CGPathAddLineToPoint(path, nil, 26 - offsetX, 9 - offsetY) 
        CGPathAddLineToPoint(path, nil, 28 - offsetX, 6 - offsetY) 
        CGPathAddLineToPoint(path, nil, 26 - offsetX, 2 - offsetY) 
        
        CGPathCloseSubpath(path) 
        
        spriteNode.physicsBody = SKPhysicsBody(polygonFromPath: path)
        spriteNode.physicsBody?.categoryBitMask = PhysicsCategory.Player
        spriteNode.physicsBody?.collisionBitMask = 0
        spriteNode.physicsBody?.contactTestBitMask = PhysicsCategory.Obstacle | PhysicsCategory.Ground
    }
    
}