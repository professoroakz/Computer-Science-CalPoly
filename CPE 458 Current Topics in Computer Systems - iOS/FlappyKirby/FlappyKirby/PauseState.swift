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
        //
    }
    
    override func willExitWithNextState(nextState: GKState) {
        // Remove tutorial
        scene.rootNode.enumerateChildNodesWithName("Tutorial", usingBlock: { node, stop in
            node.runAction(SKAction.sequence([
                SKAction.fadeOutWithDuration(0.5),
                SKAction.removeFromParent()
                ]))
        })
    }
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return stateClass is PlayingState.Type
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        
    }
    
    func setupPause() {
        let tutorial = SKSpriteNode(imageNamed: "Tutorial")
        tutorial.position = CGPoint(x: scene.size.width * 0.5, y: scene.playableHeight * 0.4 + scene.playableStart)
        tutorial.name = "Tutorial"
        tutorial.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(tutorial)
        
        let ready = SKSpriteNode(imageNamed: "Ready")
        ready.position = CGPoint(x: scene.size.width * 0.5, y: scene.playableHeight * 0.7 + scene.playableStart)
        ready.name = "Tutorial"
        ready.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(ready)
        
    }
}