//
//  GameScene.swift
//  FlappyKirby
//
//  Created by Oktay Gardener on 05/01/16.
//  Copyright (c) 2016 Oktay Gardener. All rights reserved.
//

import SpriteKit
import AVKit
import AVFoundation
import UIKit
import GameKit

enum Layer: CGFloat {
    case Background
    case Obstacle
    case Foreground
    case Player
    case UI
}

enum KirbyState: NSInteger {
    case None
    case First
    case Second
    case Third
}

struct PhysicsCategory {
    static let None: UInt32 = 0
    static let Player: UInt32 = 0b1
    static let Obstacle: UInt32 = 0b10
    static let Ground: UInt32 = 0b100
}

class GameScene: SKScene, SKPhysicsContactDelegate {
        
    let worldNode = SKNode() // Parent node/container
    let player = Player(imageName: "Kirby0")
    
    var initalState: AnyClass
    
    init(size: CGSize, stateClass: AnyClass) {
        initalState = stateClass
        super.init(size: size)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    var scoreLabel: SKLabelNode!
    var playAgainLabel: SKLabelNode!
    var tapToBeginLabel: SKLabelNode!
    let tapToResumeLabel: SKLabelNode! = SKLabelNode(fileNamed: "Pause")
    
    var score: Int = 0
    let numberOfForegrounds: Int = 2
    
    var fontName: String = "Mono"

    var playableStart: CGFloat = 0.0
    var playableHeight: CGFloat = 0.0
    let bottomObstacleMinFraction: CGFloat = 0.1
    let bottomObstacleMaxFraction: CGFloat = 0.6
    let gapMultiplier: CGFloat = 3.5
    let groundSpeed: CGFloat = 150.0
    let margin: CGFloat = 20.0
    
    var lastUpdateTime: NSTimeInterval = 0
    var deltaTime: NSTimeInterval = 0
    
    let dingAction = SKAction.playSoundFileNamed("ding.wav", waitForCompletion: false)
    let popAction = SKAction.playSoundFileNamed("pop.wav", waitForCompletion: false)
    let coinAction = SKAction.playSoundFileNamed("coin.wav", waitForCompletion: false)
    let flapAction = SKAction.playSoundFileNamed("flapping.wav", waitForCompletion: false)
    let backgroundMusic = SKAction.playSoundFileNamed("kirby.mp3", waitForCompletion: false)
    
    let firstSpawnDelay: NSTimeInterval = 1.75
    let spawnDelay: NSTimeInterval = 1.5
    
    var hitGround: Bool = false
    var hitObstacle: Bool = false
    var backgroundMusicIsPlaying: Bool = false
    
    lazy var gameState: GKStateMachine = GKStateMachine(states: [
        MainMenuState(scene: self),
        TutorialState(scene: self),
        PlayingState(scene: self),
        FallingState(scene: self),
        GameOverState(scene: self)
    ])

    override func didMoveToView(view: SKView) {
        NSNotificationCenter.defaultCenter().addObserver(self,
            selector: Selector("pauseGameScene"),
            name: UIApplicationWillResignActiveNotification,
            object: nil)
        physicsWorld.gravity = CGVector(dx: 0, dy: 0)
        physicsWorld.contactDelegate = self
        
        addChild(worldNode)
        gameState.enterState(initalState)
    }
    
    
    // MARK: Setup methods
    
    func setupBackgrounds() {
        // randomize backgrounds later from array
        let background = SKSpriteNode(imageNamed: "Background 1")
        background.anchorPoint = CGPoint(x: 0.5, y: 1.0) // center
        background.position = CGPoint(x: size.width/2, y: size.height)
        background.zPosition = Layer.Background.rawValue
        
        worldNode.addChild(background)
        
        playableStart = size.height - background.size.height
        playableHeight = background.size.height
        
        
        let lowerLeft = CGPoint(x: 0, y: playableStart)
        let lowerRight = CGPoint(x: size.width, y: playableStart)
        
        physicsBody = SKPhysicsBody(edgeFromPoint: lowerLeft, toPoint: lowerRight)
        physicsBody?.categoryBitMask = PhysicsCategory.Ground
        physicsBody?.collisionBitMask = 0
        physicsBody?.contactTestBitMask = PhysicsCategory.Player
    }
    
    func setupForeground() {
        for i in 0..<numberOfForegrounds {
            let foreground = SKSpriteNode(imageNamed: "Ground 1")
            foreground.anchorPoint = CGPoint(x: 0.0, y: 1.0)
            foreground.position = CGPoint(x: CGFloat(i) * size.width, y: playableStart)
            foreground.zPosition = Layer.Foreground.rawValue
            foreground.name = "Foreground"
            
            worldNode.addChild(foreground)
        }
    }
    
    func setupPlayer() {
        let playerNode = player.spriteComponent.node
        playerNode.position = CGPoint(x: size.width * 0.2, y: playableHeight * 0.4 + playableStart)
        playerNode.zPosition = Layer.Player.rawValue
        worldNode.addChild(playerNode)
        
        player.movementComponent.playableStart = playableStart
    }
    
    func setupScoreLabel() {
        scoreLabel = SKLabelNode(fontNamed: fontName)
        
        scoreLabel.position = CGPoint(x: size.width/2, y: size.height - margin)
        scoreLabel.verticalAlignmentMode = .Top
        scoreLabel.zPosition = Layer.UI.rawValue
        
        scoreLabel.fontColor = SKColor.whiteColor()
        scoreLabel.text = "\(score)"
        
        worldNode.addChild(scoreLabel)
    }
    
    func startSpawningObstacles() {
        let firstDelay = SKAction.waitForDuration(firstSpawnDelay)
        let spawn = SKAction.runBlock(spawnObstacle)
        let everyDelay = SKAction.waitForDuration(spawnDelay)
        
        
        let spawnSequence = SKAction.sequence([spawn, everyDelay])
        let foreverSpawn = SKAction.repeatActionForever(spawnSequence)
        let overallSequence = SKAction.sequence([firstDelay, foreverSpawn])
        
        runAction(overallSequence, withKey: "spawn")
    }
    
    func stopSpawningObstacles() {
        removeActionForKey("spawn")
        worldNode.enumerateChildNodesWithName("Obstacle", usingBlock: {node, stop in
            node.removeAllActions()
        })
    }
    
    func createObstacle() -> SKSpriteNode {
        let obstacle = Obstacle(imageName: "Obstacle")
        let obstacleNode = obstacle.spriteComponent.node
        obstacleNode.zPosition = Layer.Obstacle.rawValue
        obstacleNode.name = "Obstacle"
        
        obstacleNode.userData = NSMutableDictionary()
        
        return obstacle.spriteComponent.node
    }
    
    
    func spawnObstacle() {
        // Bottom obstacle
        let bottomObstacle = createObstacle()
        let startX = size.width + bottomObstacle.size.width/2
        
        let bottomObstacleMin = (playableStart - bottomObstacle.size.height/2) + playableHeight * bottomObstacleMinFraction
        let bottomObstacleMax = (playableStart - bottomObstacle.size.height/2) + playableHeight * bottomObstacleMaxFraction
        
        // Using GameplayKit's randomization
        let randomSource = GKARC4RandomSource()
        let randomDistribution = GKRandomDistribution(randomSource: randomSource, lowestValue: Int(round(bottomObstacleMin)), highestValue: Int(round(bottomObstacleMax)))
        let randomValue = randomDistribution.nextInt()
        
        bottomObstacle.position = CGPointMake(startX, CGFloat(randomValue))
        worldNode.addChild(bottomObstacle)
        
        // Top obstacle
        let topObstacle = createObstacle()
        topObstacle.zRotation = CGFloat(180).degreesToRadians()
        topObstacle.position = CGPoint(x: startX, y: bottomObstacle.position.y + bottomObstacle.size.height/2 + topObstacle.size.height/2 + player.spriteComponent.node.size.height * gapMultiplier)
        worldNode.addChild(topObstacle)
        
        let moveX = size.width + topObstacle.size.width
        let moveDuration = moveX / groundSpeed
        
        let sequence = SKAction.sequence([
            SKAction.moveByX(-moveX, y: 0, duration: NSTimeInterval(moveDuration)),
            SKAction.removeFromParent()
            ])
        
        topObstacle.runAction(sequence)
        bottomObstacle.runAction(sequence)
    }
    
    // MARK: Gameplay
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        switch gameState.currentState {
        case is MainMenuState:
            restartGame(TutorialState)
        case is TutorialState:
            gameState.enterState(PlayingState)
        case is PlayingState:
          //  if !backgroundMusicIsPlaying {
  //           //   worldNode.runAction(backgroundMusic, withKey: "BackgroundMusic")
    //            worldNode.runAction(backgroundMusic)
      //          backgroundMusicIsPlaying = true
        //    }
            player.movementComponent.applyImpulse()
            runAction(flapAction)
        case is GameOverState:
            restartGame(TutorialState)
        default:
            break
        }
    }
    
    func restartGame(stateClass: AnyClass) {
        runAction(popAction)
        let newScene = GameScene(size: size, stateClass: stateClass)
        let transition = SKTransition.fadeWithColor(SKColor.blackColor(), duration: 0.4)
        view?.presentScene(newScene, transition: transition)
        view?.scene?.removeAllChildren()

    }
    
    // MARK: Physics
    
    func didBeginContact(contact: SKPhysicsContact) {
        let other = contact.bodyA.categoryBitMask == PhysicsCategory.Player ? contact.bodyB : contact.bodyA
        
        if other.categoryBitMask == PhysicsCategory.Ground {
            gameState.enterState(GameOverState)
        }
        
        if other.categoryBitMask == PhysicsCategory.Obstacle {
            gameState.enterState(FallingState)
        }
    }
    
    
    // MARK: Updates
   
    override func update(currentTime: CFTimeInterval) {
        if lastUpdateTime == 0 {
            lastUpdateTime = currentTime
        }
        
        deltaTime = currentTime - lastUpdateTime
        lastUpdateTime = currentTime
        
        gameState.updateWithDeltaTime(deltaTime)
        player.updateWithDeltaTime(deltaTime)

    }
    
    func updateScore() {
        worldNode.enumerateChildNodesWithName("Obstacle", usingBlock: {node, stop in
            if let obstacle = node as? SKSpriteNode {
                if let passed = obstacle.userData?["Passed"] as? NSNumber {
                    if passed.boolValue {
                        return
                    }
                }
                
                if self.player.spriteComponent.node.position.x > obstacle.position.x + obstacle.size.width/2 {
                    self.score++
                    self.scoreLabel.text = "\(self.score / 2)"
                    
                    obstacle.userData?["Passed"] = NSNumber(bool: true)
                    self.runAction(self.coinAction)
                }
            }
        })
    }
    
    func updateForeground() {
        worldNode.enumerateChildNodesWithName("Foreground", usingBlock: { node, stop in
            if let foreground = node as? SKSpriteNode {
                let moveAmount = CGPoint(x: -self.groundSpeed * CGFloat(self.deltaTime), y: 0)
                foreground.position.x += moveAmount.x
                foreground.position.y += moveAmount.y
                
                if foreground.position.x < -foreground.size.width {
                    let increaseForeground: CGPoint = CGPoint(x: foreground.size.width * CGFloat(self.numberOfForegrounds), y: 0)
                    foreground.position.x += increaseForeground.x
                    foreground.position.y += increaseForeground.y
                    
                }
            }
        })
    }
    
    // MARK: Pause
    
    func pauseGameScene() {
        self.view?.paused = true
    }
    
    func showPauseText() {
        if self.view?.paused == true {
            tapToResume.hidden = false
        }
    }
}
