//
//  GameOverState.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 27/01/16.
//  Copyright © 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import GameplayKit

class GameOverState: GKState {
    unowned let scene: GameScene
    let hitGroundAction = SKAction.playSoundFileNamed("hitGround.wav", waitForCompletion: false)
    let animationDelay = 0.2
    let numHighscores = 3
    
    init(scene: SKScene) {
        self.scene = scene as! GameScene
        super.init()
    }
    
    override func didEnterWithPreviousState(previousState: GKState?) {
        scene.runAction(hitGroundAction)
        scene.stopSpawningObstacles()
        
        scene.player.movementAllowed = false
        updateHighScores()
        showScoreCard()
    }
    
    override func isValidNextState(stateClass: AnyClass) -> Bool {
        return stateClass is PlayingState.Type
    }
    
    override func updateWithDeltaTime(seconds: NSTimeInterval) {
        
    }
    
    func setHighScore(highscores: Array<Int>) {
        for i in 0..<highscores.count {
            NSUserDefaults.standardUserDefaults().setInteger(highscores[i], forKey: "HighScore_\(i)") // div 2 obstacle counts as 2 points each
        }
    }
    
    func getHighScores() -> Array<Int> {

        var highScores: Array<Int> = []
        for i in 0..<numHighscores {
            highScores.append(NSUserDefaults.standardUserDefaults().integerForKey("HighScore_\(i)"))
        }
        
        return highScores
    }
    
    func updateHighScores() {
        var highScores: Array<Int> = getHighScores()
        for i in 0..<highScores.count {
            if scene.score/2 > highScores[i] {
                if scene.score == highScores[i] {
                    break
                }
                highScores.insert(scene.score/2, atIndex: i)
                break
            }
        }
        setHighScore(highScores)
    }

    func showScoreCard() {
        
        let scoreCard = SKSpriteNode(imageNamed: "ScoreCard")
        scoreCard.position = CGPoint(x: scene.size.width * 0.5, y: scene.size.height * 0.5)
        scoreCard.name = "ScoreCard"
        scoreCard.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(scoreCard)
        
        let lastScore = SKLabelNode(fontNamed: scene.fontName)
        lastScore.fontColor = .blackColor()
        lastScore.position = CGPoint(x: -scoreCard.size.width * 0.25, y: -scoreCard.size.width * 0.2 + scene.margin * 1.5)
        lastScore.zPosition = Layer.UI.rawValue
        lastScore.text = "\(scene.score / 2)"
        scoreCard.addChild(lastScore)
    
        let highScore = SKLabelNode(fontNamed: scene.fontName)
        highScore.fontColor = .blackColor()
        highScore.position = CGPoint(x: scoreCard.size.width * 0.20, y: -scoreCard.size.width * 0.2 + scene.margin * 1.55)
        highScore.zPosition = Layer.UI.rawValue
        
        highScore.text = getHighScores().map({"\($0)"}).joinWithSeparator(" ") // yas yas yas map put in werk
        highScore.fontSize = 24
        scoreCard.addChild(highScore)
        
        let gameOverLabel = SKSpriteNode(imageNamed: "GameOver")
        gameOverLabel.position = CGPoint(
            x: scene.size.width / 2,
            y: scene.size.height / 2 + scoreCard.size.height / 2
                + scene.margin + gameOverLabel.size.height / 2)
        
        gameOverLabel.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(gameOverLabel)
        
        let okButton = SKSpriteNode(imageNamed: "GameOverButton")
        okButton.position = CGPoint(x: scene.size.width * 0.25,
            y: scene.size.height / 2 - scoreCard.size.height / 2
                - scene.margin - okButton.size.height / 2)
        okButton.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(okButton)
        
        let ok = SKSpriteNode(imageNamed: "OK")
        ok.position = CGPoint(x: 0, y: 5)
        ok.zPosition = Layer.UI.rawValue
        
        okButton.addChild(ok)
        
        let mainMenuButton = SKSpriteNode(imageNamed: "GameOverButton")
        mainMenuButton.position = CGPoint(x: scene.size.width * 0.75,
            y: scene.size.height / 2 - scoreCard.size.height / 2
                - scene.margin - mainMenuButton.size.height / 2)
        mainMenuButton.zPosition = Layer.UI.rawValue
        scene.rootNode.addChild(mainMenuButton)
        
        let mainMenu = SKSpriteNode(imageNamed: "MainMenu")
        mainMenu.position = CGPoint(x: 0, y: 5)
        mainMenu.zPosition = Layer.UI.rawValue
        mainMenuButton.addChild(mainMenu)
        
        setupScoreCardAnimations(scoreCard, gameOverLabel: gameOverLabel, okButton: okButton, mainMenuButton: mainMenuButton)
    }
    
    func setupScoreCardAnimations(scoreCard: SKSpriteNode, gameOverLabel: SKSpriteNode, okButton: SKSpriteNode, mainMenuButton: SKSpriteNode) {
        gameOverLabel.setScale(0)
        gameOverLabel.alpha = 0
        let group = SKAction.group([
            SKAction.fadeInWithDuration(animationDelay),
            SKAction.scaleTo(1.0, duration: animationDelay)
            ])
        group.timingMode = .EaseIn
        gameOverLabel.runAction(SKAction.sequence([
            SKAction.waitForDuration(animationDelay),
            group
            ]))
        
        scoreCard.position = CGPoint(x: scene.size.width * 0.5, y: -scoreCard.size.height/2)
        let moveTo = SKAction.moveTo(CGPoint(x: scene.size.width/2, y: scene.size.height/2), duration: animationDelay)
        moveTo.timingMode = .EaseInEaseOut
        scoreCard.runAction(SKAction.sequence([
            SKAction.waitForDuration(animationDelay * 2),
            moveTo
            ]))
        
        okButton.alpha = 0
        mainMenuButton.alpha = 0
        let fadeIn = SKAction.sequence([
            SKAction.waitForDuration(animationDelay * 4),
            SKAction.fadeInWithDuration(animationDelay)
            ])
        
        okButton.runAction(fadeIn)
        mainMenuButton.runAction(fadeIn)
        
        let pops = SKAction.sequence([
            SKAction.waitForDuration(animationDelay),
            scene.popAction,
            SKAction.waitForDuration(animationDelay),
            scene.popAction,
            SKAction.waitForDuration(animationDelay),
            scene.popAction
            ])
        scene.runAction(pops)
    }
}











