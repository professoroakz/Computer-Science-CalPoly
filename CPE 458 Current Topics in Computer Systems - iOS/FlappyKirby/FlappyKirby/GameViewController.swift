//
//  GameViewController.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 05/01/16.
//  Copyright (c) 2016 Oktay Gardener. All rights reserved.
//

import UIKit
import SpriteKit

class GameViewController: UIViewController {
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        
        if let skView = self.view as? SKView {
            if skView.scene == nil {
                // create scene
                let aspectRatio = skView.bounds.size.height / skView.bounds.size.width
                
                let scene = GameScene(size:CGSize(width: 320, height: 320 * aspectRatio))
                
                skView.showsFPS = true
                skView.showsNodeCount = true
                skView.showsPhysics = true
                skView.ignoresSiblingOrder = true // faster
                scene.scaleMode = .AspectFill
                
                skView.presentScene(scene)
                
            }
        }
    }
    
    override func prefersStatusBarHidden() -> Bool {
        return true
    }
}