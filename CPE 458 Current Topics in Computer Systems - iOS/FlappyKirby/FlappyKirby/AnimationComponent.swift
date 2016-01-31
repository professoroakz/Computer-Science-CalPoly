//
//  AnimationComponent.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 30/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class AnimationComponent: GKComponent {
    let spriteComponent: SpriteComponent
    var textures: Array<SKTexture>
    
    init(entity: GKEntity, textues: Array<SKTexture>) {
        self.textures = textues
        self.spriteComponent = entity.componentForClass(SpriteComponent)!
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        if let player = entity as? Player {
            if player.movementAllowed {
                startAnimation()
            } else {
                stopAnimation()
            }
        }
    }
    
    func startAnimation() {
        if !spriteComponent.node.hasActions() {
            let playerAnimation = SKAction.animateWithTextures(textures, timePerFrame: 0.07)
            spriteComponent.node.runAction(SKAction.repeatActionForever(playerAnimation))
        }
    }
    
    func stopAnimation() {
        spriteComponent.node.removeAllActions()
    }
}
