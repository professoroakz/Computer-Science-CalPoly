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
    let backgroundMusic = SKAudioNode(fileNamed: "kirby-2.mp3")
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnterWithPreviousState(previousState: GKState?) {
        scene.startSpawningObstacles()
        scene.setupInGamePauseButton()
        scene.player.movementAllowed = true
    }
    
    
    override func willExitWithNextState(nextState: GKState) {
      //  scene.rootNode.removeFromParent(backgroundNode)
        scene.pause.removeFromParent()
    }
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return (stateClass == FallingState.self) || (stateClass ==  GameOverState.self) || (stateClass == PauseState.self)
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        scene.updateBackground()
        scene.updateForeground()
        scene.updateScore()
    }
    
    func startMusic() {
        backgroundMusic.autoplayLooped = true
        scene.rootNode.addChild(backgroundMusic)
    }
}