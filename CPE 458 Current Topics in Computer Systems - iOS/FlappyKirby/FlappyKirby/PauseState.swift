//
//  PauseState.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 28/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class PauseState: GKState {
    unowned let scene: GameScene
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnterWithPreviousState(previousState: GKState?) {
        setupPauseButton()
        scene.speed = 0
        scene.player.movementAllowed = false
    }
    
    override func willExitWithNextState(nextState: GKState) {
        resumeGame()
    }
    
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return stateClass is PlayingState.Type
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        
    }
    
    func setupPauseButton() {
        scene.player.movementAllowed = false
        
        let pause = SKSpriteNode(imageNamed: "Pause")
        pause.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height * 0.5)
        pause.zPosition = Layer.UI.rawValue
        pause.name = "Pause"
        
        scene.rootNode.addChild(pause)
        
        let pauseLabel = SKLabelNode(fontNamed: scene.fontName)
        
        pauseLabel.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height * 0.5 + scene.margin * 3)
        pauseLabel.verticalAlignmentMode = .Top
        pauseLabel.zPosition = Layer.UI.rawValue
        pauseLabel.text = "Paused"
        pauseLabel.name = "Pause"
        
        pauseLabel.fontColor = SKColor.whiteColor()
        
        scene.rootNode.addChild(pauseLabel)
    }
    
    func resumeGame() {
        scene.removeActionForKey("Pause")
        scene.rootNode.enumerateChildNodesWithName("Pause", usingBlock: {node, stop in
            node.removeFromParent()
        })
        
        scene.speed = 1
        scene.player.movementAllowed = true
    }
}