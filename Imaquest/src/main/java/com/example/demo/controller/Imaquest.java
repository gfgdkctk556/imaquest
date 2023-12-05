//package com.example.demo.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.example.demo.entity.HealerCharacterEntity;
//import com.example.demo.entity.PlayerCharacterEntity;
//import com.example.demo.entity.TankCharacterEntity;
//import com.example.demo.entity.WizardCharacterEntity;
//import com.example.demo.repository.HealerCharacterRepository;
//import com.example.demo.repository.PlayerCharacterRepository;
//import com.example.demo.repository.TankCharacterRepository;
//import com.example.demo.repository.WizardCharacterRepository;
//
//@Controller
//public class Imaquest {
//
//	@Autowired
//	private PlayerCharacterRepository playerCharacterRepository;
//
//	@Autowired
//	private TankCharacterRepository tankCharacterRepository;
//
//	@Autowired
//	private WizardCharacterRepository wizardCharacterRepository;
//
//	@Autowired
//	private HealerCharacterRepository healerCharacterRepository;
//
//	@GetMapping("/playerList")
//	public String playerList(Model model) {
//		List<PlayerCharacterEntity> players = playerCharacterRepository.findAll();
//		model.addAttribute("players", players);
//		return "player/playerList";
//	}
//
//	@GetMapping("/tankList")
//	public String tankList(Model model) {
//		List<TankCharacterEntity> tanks = tankCharacterRepository.findAll();
//		model.addAttribute("tanks", tanks);
//		return "tank/tankList";
//	}
//
//	@GetMapping("/wizardList")
//	public String wizardList(Model model) {
//		List<WizardCharacterEntity> wizards = wizardCharacterRepository.findAll();
//		model.addAttribute("wizards", wizards);
//		return "wizard/wizardList";
//	}
//
//	@GetMapping("/healerList")
//	public String healerList(Model model) {
//		List<HealerCharacterEntity> healers = healerCharacterRepository.findAll();
//		model.addAttribute("healers", healers);
//		return "healer/healerList";
//	}
//
//	@GetMapping("/player/create")
//	public String createPlayerForm(Model model) {
//		model.addAttribute("player", new PlayerCharacterEntity());
//		return "player/createPlayer";
//	}
//
//	@PostMapping("/player/create")
//	public String createPlayer(@ModelAttribute PlayerCharacterEntity player) {
//		playerCharacterRepository.save(player);
//		return "redirect:/imaquest/playerList";
//	}
//
//	@GetMapping("/tank/create")
//	public String createTankForm(Model model) {
//		model.addAttribute("tank", new TankCharacterEntity());
//		return "tank/createTank";
//	}
//
//	@PostMapping("/tank/create")
//	public String createTank(@ModelAttribute TankCharacterEntity tank) {
//		tankCharacterRepository.save(tank);
//		return "redirect:/imaquest/tankList";
//	}
//
//	@GetMapping("/wizard/create")
//	public String createWizardForm(Model model) {
//		model.addAttribute("wizard", new WizardCharacterEntity());
//		return "wizard/createWizard";
//	}
//
//	@PostMapping("/wizard/create")
//	public String createWizard(@ModelAttribute WizardCharacterEntity wizard) {
//		wizardCharacterRepository.save(wizard);
//		return "redirect:/imaquest/wizardList";
//	}
//
//	@GetMapping("/healer/create")
//	public String createHealerForm(Model model) {
//		model.addAttribute("healer", new HealerCharacterEntity());
//		return "healer/createHealer";
//	}
//
//	@PostMapping("/healer/create")
//	public String createHealer(@ModelAttribute HealerCharacterEntity healer) {
//		healerCharacterRepository.save(healer);
//		return "redirect:/imaquest/healerList";
//	}
//
//	@RequestMapping(path="/login",method=RequestMethod.GET)
//	public String loginForm(Model model) {
//		System.out.println("test");
//		return "login"; // login.htmlのパス
//	}
//
//	@PostMapping("/login")
//	public String login(@RequestParam String playerId, @RequestParam String password) {
//		// ログイン処理を実装
//		// プレイヤーIDとパスワードの検証が必要
//		return "redirect:/imaquest/playerList"; // ログイン後のリダイレクト先
//	}
//
//	@GetMapping("/register")
//	public String registerForm(Model model) {
//		return "register"; // register.htmlのパス
//	}
//
//	@PostMapping("/register")
//	public String register(@RequestParam("player_Id") String player_Id, @RequestParam("password") String password) {
//	    // アカウント登録処理を実装
//	    // プレイヤーIDとパスワードをデータベースに保存
//
//	    // 新しいプレイヤーキャラクターエンティティを作成し、プレイヤーIDとパスワードを設定
//	    PlayerCharacterEntity player = new PlayerCharacterEntity();
//	    player.setPlayer_id(player_Id);
//	    player.setPlayer_pass(password);
//
//	    // その他の初期ステータス情報を設定
//	    player.setCharacter_Name("yuusya");
//	    player.setCharacter_Level(1); // プレイヤーの初期レベル
//	    player.setCharacter_Experience(0);
//	    player.setCharacter_HP(100); // 仮の初期値
//	    player.setCharacter_MP(50); // 仮の初期値
//	    player.setCharacter_Attack(10); // 仮の初期値
//	    player.setCharacter_Defense(5); // 仮の初期値
//	    player.setCharacter_Image("default_player_image.png");
//
//	    // プレイヤーキャラクターエンティティを保存
//	    playerCharacterRepository.save(player);
//
//		// タンクの初期キャラクターを作成
//		TankCharacterEntity tank = new TankCharacterEntity();
//		tank.setCharacter_Name("Tanks"); // タンクの名前
//		tank.setCharacter_Level(1); // タンクの初期レベル
//		// タンクのステータスを設定
//		tank.setCharacter_HP(150);
//		tank.setCharacter_MP(30);
//		tank.setCharacter_Attack(8);
//		tank.setCharacter_Defense(15);
//		tank.setCharacter_Image("tank_image.png");
//		tankCharacterRepository.save(tank);
//
//		// 回復士の初期キャラクターを作成
//		HealerCharacterEntity healer = new HealerCharacterEntity();
//		healer.setCharacter_Name("Healers"); // 回復士の名前
//		healer.setCharacter_Level(1); // 回復士の初期レベル
//		// 回復士のステータスを設定
//		healer.setCharacter_HP(80);
//		healer.setCharacter_MP(60);
//		healer.setCharacter_Attack(6);
//		healer.setCharacter_Defense(10);
//		healer.setCharacter_Image("healer_image.png");
//		healerCharacterRepository.save(healer);
//
//		// 魔法使いの初期キャラクターを作成
//		WizardCharacterEntity wizard = new WizardCharacterEntity();
//		wizard.setCharacter_Name("Wizards"); // 魔法使いの名前
//		wizard.setCharacter_Level(1); // 魔法使いの初期レベル
//		// 魔法使いのステータスを設定
//		wizard.setCharacter_HP(70);
//		wizard.setCharacter_MP(80);
//		wizard.setCharacter_Attack(5);
//		wizard.setCharacter_Defense(8);
//		wizard.setCharacter_Image("wizard_image.png");
//		wizardCharacterRepository.save(wizard);
//
//		return "redirect:/imaquest/login"; // 登録後にログイン画面にリダイレクト
//	}
//}
