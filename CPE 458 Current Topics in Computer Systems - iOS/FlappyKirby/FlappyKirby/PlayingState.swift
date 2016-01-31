//
//  PlayingState.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 27/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class PlayingState: GKState {
    unowned let scene: GameScene
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnterWithPreviousState(previousState: GKState?) {
        scene.startSpawningObstacles()
        scene.player.movementAllowed = true
    }
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return (stateClass == FallingState.self) || (stateClass ==  GameOverState.self)
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        scene.updateBackground()
        scene.updateForeground()
        scene.updateScore()
    }
}