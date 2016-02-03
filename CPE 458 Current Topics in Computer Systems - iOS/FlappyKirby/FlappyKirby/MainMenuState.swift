//
//  MainMenuState.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 27/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class MainMenuState: GKState {
    unowned let scene: GameScene
    let hitGroundAction = SKAction.playSoundFileNamed("hitGround.wav", waitForCompletion: false)
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnterWithPreviousState(previousState: GKState?) {
        scene.setupBackgrounds()
        scene.setupForeground()
        scene.setupPlayer()
        
        showMainMenu()
        
        scene.player.movementAllowed = false
    }
    
    override func willExitWithNextState(nextState: GKState) {
        
    }
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return stateClass is TutorialState.Type
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        
    }
    
    func showMainMenu() {
        let logo = SKSpriteNode(imageNamed: "Logo")
        
        logo.position = CGPoint(x: scene.size.width / 2, y: scene.size.height * 0.8)
        logo.zPosition = Layer.UI.rawValue
        
        scene.rootNode.addChild(logo)
        
        let playButton = SKSpriteNode(imageNamed: "Button")
        
        playButton.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height / 2)
        playButton.zPosition = Layer.UI.rawValue
        
        let play = SKSpriteNode(imageNamed: "Play")
        play.position = CGPoint(x: 0, y: scene.margin/2)
        
        let scaleUp = SKAction.scaleTo(1.05, duration: 0.75)
        scaleUp.timingMode = .EaseInEaseOut
        let scaleDown = SKAction.scaleTo(0.95, duration: 0.75)
        scaleDown.timingMode = .EaseInEaseOut
        
        play.runAction(SKAction.repeatActionForever(SKAction.sequence([scaleUp, scaleDown])))

        playButton.addChild(play)
        
        scene.rootNode.addChild(playButton)
        
        let learn = SKSpriteNode(imageNamed: "Github")
        learn.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height / 2)

        learn.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height * 0.10)
        learn.zPosition = Layer.UI.rawValue
        
        scene.rootNode.addChild(learn)

        
    }
}
