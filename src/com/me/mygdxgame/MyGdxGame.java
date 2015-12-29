package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

import mtear.lib.GameBase;
import mtear.lib.NBitmap;

public class MyGdxGame extends GameBase{
	final static int GAMEOVER1 = 0, GAMEOVER2 = 1, PLAY = 2, MENU = 3, RESETTING = 4, TUTORIAL = 5, SCOREVIEW = 6;
	static int GAMESTATE = 3;
	
	private NBitmap one, two, three, six, twelve, twentyfour, fourtyeight, ninetysix, n192, n384, n768, n1536, n3072, n6144;
	private NBitmap sixp, twelvep, twentyfourp, fourtyeightp, ninetysixp, n192p, n384p, n768p, n1536p, n3072p, n6144p;
	private NBitmap gboard, next1, next2, next3, next0, rotateanimation, drotateanimation;
	private NBitmap onegrey, twogrey, treyimage, redcard, redcardgrey, nextorange;
	private float[] gridx, gridy;
	BitmapFont genfont, genfont2, yhl;
	Sound swipesound, alerttone, combinesound, newksound, scorechime,sound3000,sound6144;
	Music bgmusic;
	ArrayList<Point> rotatepoints = new ArrayList<Point>();
	TextureAtlas rotateatlas, drotateatlas;
	private int[][] gameboard = new int[4][4];
	HashMap<Integer, NBitmap> imagemap = new HashMap<Integer, NBitmap>();
	int nexttype = -1, hadthistype = 0, tempscore = 0, rotateframetimer=0, rotatecurrentframe = 0, flyx=0, flyy=0, flydir = 0,flytimer=0;
	float offsetxstart = 0, offsetx = 0, offsetystart = 0, offsety = 0, flyposx=0, flyposy=0, flyxv=0,flyxv2=0,flyyv =0,flyyv2=0;
	NBitmap flysprite = null, helpicon, nosound, sound1, sound2, orangecard, trophy;
	boolean xswipe = true, swiping = false, alive = false, flyingin = false;
	int flytotaltime = 8;
	int highestk = 0;
	boolean jumping = false, queuejump = false;
	int jumptimer = 0, maxjump=20;
	boolean isbuzzing = false, darkrotating = false;
	int buzztimer = 0; int maxbuzz = 4;
	boolean swipeguard = false;
	int swipeguardtimer = 0;
	float treyx=0, boardx=0, treyboardvx=0; int maxintroframes = 15, introanimtimer = 0, resettimer = 0; boolean duringintrotimer = false, resetting = false;
	boolean scoreanimating = false, acceptanotherscore=false; float stextx = 0, stexty=0, stextvx = 0, stextvy = 0;
	int scoreslefttoanimate=0, animatescoretimer =0,maxscoreframe=25, totalholdingscore = 0;
	LinkedList<Point> scoreanimatepoints = new LinkedList<Point>();
	int zerocooldown = 0, tutorialtimer=0, tutorialswipes=3, currentmessage=0;
	String[] tutorialmessages = {"welcome to trey", "swipe to move tiles", "1 + 2 = 3", "3 + 3 = 6", "1 only combines with 2", "red tiles are wild", "orange and white make", "good luck"};
	boolean pastcontrolledtutorial = false, completedtutorial=false, preparescoreview = false;
	int soundmode = 2, superpower = 0, scorestate = 0;
	Preferences _savesettings;
	String highestboardstring = "";
	
	int[] highscores = new int[5];
	
	@Override
	public void init() {
		//settings
		_savesettings = Gdx.app.getPreferences("savesettings");
		soundmode = _savesettings.getInteger("soundmode", 2);
		completedtutorial = _savesettings.getBoolean("completedtutorial", false);
		for(int i = 0; i < highscores.length; i++){
			highscores[i] = _savesettings.getInteger("highscore"+i, 0);
			System.out.println("Score " + i + ": " + highscores[i]);
		}
		highestboardstring = _savesettings.getString("highestboardstring", "-1;-1;-1;-1;~-1;-1;-1;-1;~-1;-1;-1;-1;~-1;-1;-1;-1;");
		
		gameboard = clearBoard(gameboard);
		float [] gx = {_W/8.5f, _W/8.5f+_W/4.97f, _W/8.5f+2*_W/4.97f, _W/8.5f+3*_W/4.97f}; gridx = gx;
		float [] gy = {_H/6, _H/6+_H/6.6f, _H/6+2*_H/6.6f, _H/6+3*_H/6.6f}; gridy = gy;
		this.setClearColor(1, 1, 1);
		
		readyImages();
		genfont = new BitmapFont(Gdx.files.internal("data/gentext.fnt"),false);
		genfont.setColor(new Color(200,200,200,1));
		genfont.setScale((((float)_W)*.001f));
		genfont2 = new BitmapFont(Gdx.files.internal("data/gentext.fnt"),false);
		genfont2.setColor(new Color(200,200,200,1));
		genfont2.setScale((((float)_W)*.0005f));
		yhl = new BitmapFont(Gdx.files.internal("data/yhl.fnt"),false);
		yhl.setScale((((float)_W)*.0015f));
		
		flyxv = ((_W/2-gboard.getWidth()/2)+one.getWidth())/flytotaltime;
		flyxv2 = (_W-gridx[3])/flytotaltime;
		flyyv = (gridy[0]+one.getHeight())/flytotaltime;
		flyyv2 = (one.getHeight()-gridy[3])/flytotaltime;
		
		swipesound = Gdx.audio.newSound(Gdx.files.internal("data/swipe.mp3"));
		alerttone = Gdx.audio.newSound(Gdx.files.internal("data/alerttone.mp3"));
		combinesound = Gdx.audio.newSound(Gdx.files.internal("data/combine.mp3"));
		newksound = Gdx.audio.newSound(Gdx.files.internal("data/newk.mp3"));
		scorechime = Gdx.audio.newSound(Gdx.files.internal("data/scorechime.mp3"));
		sound3000 = Gdx.audio.newSound(Gdx.files.internal("data/3000sound.mp3"));
		sound6144 = Gdx.audio.newSound(Gdx.files.internal("data/6144sound.mp3"));
		
		bgmusic = Gdx.audio.newMusic(Gdx.files.internal("data/bgm.mp3"));
		bgmusic.setLooping(true);
		if(soundmode > 1)
			bgmusic.play();
	}
	
	@Override
	public void draw() {
		if(soundmode == 0) nosound.drawAt(_W-nosound.getWidth()-helpicon.getWidth()/4, helpicon.getHeight()/4+nosound.getHeight()/5);
		if(soundmode == 1) sound1.drawAt(_W-nosound.getWidth()-helpicon.getWidth()/4, helpicon.getHeight()/4+nosound.getHeight()/5);
		if(soundmode == 2) sound2.drawAt(_W-nosound.getWidth()-helpicon.getWidth()/4, helpicon.getHeight()/4+nosound.getHeight()/5);
		
		if(GAMESTATE == MENU || GAMESTATE == RESETTING){
			treyimage.drawAt(_W/2-treyimage.getWidth()/2+treyx, _H-treyimage.getHeight()*1.25f);
			if(!duringintrotimer && !resetting){
				genfont2.draw(_batch, "swipe to play", _W/2-genfont2.getBounds("swipe to play").width/2, _H/5);
			}
			
			if(duringintrotimer){
				gboard.drawAt(boardx, _H/8);
			}
		}
		if(GAMESTATE == MENU && GAMESTATE != RESETTING && GAMESTATE != PLAY && !duringintrotimer && completedtutorial){
			helpicon.drawAt(helpicon.getWidth()/4, helpicon.getHeight()/4);
			trophy.drawAt(_W/2-trophy.getWidth()/2,helpicon.getHeight()/4+nosound.getHeight()/5);
		}
		
		//draw bg
		if(GAMESTATE <= PLAY || GAMESTATE == TUTORIAL || GAMESTATE == SCOREVIEW){
			if(!preparescoreview && scorestate == 0 && !duringintrotimer)gboard.drawAt(_W/2-gboard.getWidth()/2, _H/8);
			//Draw board
			for(int i = 0; i < 4; i++){
				for(int a = 0; a < 4; a++){
					if(gameboard[i][a] >= 0){
						NBitmap todraw = imagemap.get(gameboard[i][a] + ((getK(gameboard[i][a]) == highestk && highestk > 0)?10000:0));
						if(gameboard[i][a] == 0 && superpower == 1)todraw = orangecard;
						if(GAMESTATE == GAMEOVER2 && gameboard[i][a]==1 || GAMESTATE == SCOREVIEW && gameboard[i][a]==1)todraw=onegrey;
						if(GAMESTATE == GAMEOVER2 && gameboard[i][a]==2 || GAMESTATE == SCOREVIEW && gameboard[i][a]==2)todraw=twogrey;
						if(GAMESTATE == GAMEOVER2 && gameboard[i][a]==0)todraw=redcardgrey;
						float drawx = gridx[i] + getDrawX(i,a), drawy = gridy[a] + getDrawY(i,a);
						if(jumping && getK(gameboard[i][a]) == highestk)drawy += getjumpy();
						if(isbuzzing)drawx+=getbuzzx();
						if(!isrotatingat(i,a) && !(i == flyx && a == flyy && flyingin) && !preparescoreview && scorestate == 0 && !duringintrotimer){
							todraw.drawAt(drawx, drawy);
						}
					}
				}
			}
			//drawrotation
			for(Point p : rotatepoints){
				if(!darkrotating){
					rotateanimation.setRegion(rotateatlas.findRegion("rotate" + rotatecurrentframe));
					rotateanimation.drawAt(gridx[p.x],gridy[p.y]);
				}else{
					drotateanimation.setRegion(drotateatlas.findRegion("drotate" + rotatecurrentframe));
					drotateanimation.drawAt(gridx[p.x],gridy[p.y]);
				}
			}
			//draw flyingin
			if(flyingin){
				flysprite.drawAt(flyposx, flyposy);
			}
			//draw next
			if(alive && GAMESTATE != TUTORIAL){
				int ntn = 600+nexttype;
				if(nexttype == 0 && superpower==1)ntn=610;
				imagemap.get(ntn).drawAt(_W/2-next1.getWidth()/2, _H-next1.getHeight());
			}else{
				if(GAMESTATE == GAMEOVER1){
					genfont.draw(_batch, "Out of moves!", _W/2-genfont.getBounds("Out of moves!").width/2, 11*(_H/12));
					genfont2.draw(_batch, "swipe", _W/2-genfont2.getBounds("swipe").width/2, _H/12);
				}else if(GAMESTATE == GAMEOVER2){
					genfont.setScale((((float)_W)*.002f));
					genfont.draw(_batch, String.valueOf(totalholdingscore), _W/2-genfont.getBounds(String.valueOf(totalholdingscore)).width/2, 11*(_H/12));
					genfont.setScale((((float)_W)*.001f));
					genfont2.draw(_batch, "swipe", _W/2-genfont2.getBounds("swipe").width/2, _H/12);
				}
			}
			
			//score
			if(scoreanimating){
				int nc = checkScore(gameboard[scoreanimatepoints.get(0).x][scoreanimatepoints.get(0).y]);
				yhl.draw(_batch, "+"+String.valueOf(nc), stextx-yhl.getBounds("+"+String.valueOf(nc)).width/2, stexty-yhl.getBounds("+"+String.valueOf(nc)).height/2);
			}
			//tutorial
			if(GAMESTATE == TUTORIAL){
				if(currentmessage == 0 || currentmessage == 1 || currentmessage == 4){
					genfont2.draw(_batch, "swipe", _W/2-genfont2.getBounds("swipe").width/2, _H/12);
				}
				if(currentmessage == 2){
					genfont2.draw(_batch, "make a 3!", _W/2-genfont2.getBounds("make a 3!").width/2, _H/12);
				}
				if(currentmessage == 3){
					genfont2.draw(_batch, "make a 6!", _W/2-genfont2.getBounds("make a 6!").width/2, _H/12);
				}
				if(currentmessage == 5){
					genfont2.draw(_batch, "make a 12!", _W/2-genfont2.getBounds("make a 12!").width/2, _H/12);
				}
				if(currentmessage == 6){
					genfont2.draw(_batch, "make a 24!", _W/2-genfont2.getBounds("make a 24!").width/2, _H/12);
				}
				if(currentmessage == 7){
					genfont2.draw(_batch, "don't let your board get too full", _W/2-genfont2.getBounds("don't let your board get too full").width/2, _H/12);
				}
				float scale = genfont2.getScaleX();
				if(currentmessage != 6){
					genfont2.setScale((((float)_W)*.00105f));
					if(currentmessage == 4)genfont2.setScale((((float)_W)*.001f));
					genfont2.draw(_batch, tutorialmessages[currentmessage], _W/2-genfont2.getBounds(tutorialmessages[currentmessage]).width/2, 11*(_H/12));
					genfont2.setScale(scale);
				}else if(currentmessage == 6){
					genfont2.setScale((((float)_W)*.0008f));
					genfont2.draw(_batch, tutorialmessages[6], _W/2-genfont2.getBounds(tutorialmessages[6]).width/2, 11*(_H/12));
					genfont2.draw(_batch, "your highest number", _W/2-genfont2.getBounds("your highest number").width/2, 11*(_H/12)-genfont2.getBounds(tutorialmessages[6]).height-10);
					genfont2.setScale(scale);
				}
			}
			
		}
		if(GAMESTATE == SCOREVIEW){
			if(duringintrotimer){
				if(scorestate <= 1){
					gboard.drawAt(boardx, _H/8);
				}
			}else{
				if(scorestate == 0){
					genfont2.draw(_batch, "swipe right to left to see more", _W/2-genfont2.getBounds("swipe right to left to see more").width/2, _H/12);
					genfont2.draw(_batch, "your highest score", _W/2-genfont2.getBounds("your highest score").width/2, 24*(_H/25));
					genfont.setScale((((float)_W)*.002f));
					genfont.draw(_batch, String.valueOf(totalScore()), _W/2-genfont.getBounds(String.valueOf(totalScore())).width/2, 11*(_H/12));
					genfont.setScale((((float)_W)*.001f));
				}
				if(scorestate == 1){
					genfont2.draw(_batch, "swipe left to right to go back", _W/2-genfont2.getBounds("swipe left to right to go back").width/2, _H/12);
					genfont.setScale((((float)_W)*.002f));
					genfont.draw(_batch, String.valueOf("scores"), _W/2-genfont.getBounds(String.valueOf("scores")).width/2, 11*(_H/12));
					genfont.setScale((((float)_W)*.001f));
					genfont.draw(_batch, String.valueOf("1. " + highscores[0]), _W/8, 2*(_H/3));
					genfont.draw(_batch, String.valueOf("2. " + highscores[1]), _W/8, 2*(_H/3)-1*_H/10);
					genfont.draw(_batch, String.valueOf("3. " + highscores[2]), _W/8, 2*(_H/3)-2*_H/10);
					genfont.draw(_batch, String.valueOf("4. " + highscores[3]), _W/8, 2*(_H/3)-3*_H/10);
					genfont.draw(_batch, String.valueOf("5. " + highscores[4]), _W/8, 2*(_H/3)-4*_H/10);
				}
				if(scorestate == 2){
					genfont.setScale((((float)_W)*.002f));
					genfont.draw(_batch, String.valueOf("credits"), _W/2-genfont.getBounds(String.valueOf("credits")).width/2, 11*(_H/12));
					genfont.setScale((((float)_W)*.001f));
					genfont.draw(_batch, String.valueOf("programming, art,"), _W/2-genfont.getBounds(String.valueOf("programming, art,")).width/2, 2*(_H/3));
					genfont.draw(_batch, String.valueOf("and music by"), _W/2-genfont.getBounds(String.valueOf("and music by")).width/2, 2*(_H/3)-1*_H/15);
					genfont.draw(_batch, String.valueOf("nic wilson"), _W/2-genfont.getBounds(String.valueOf("nic wilson")).width/2, 2*(_H/3)-2*_H/15);
					genfont2.draw(_batch, String.valueOf("Mtear games on Facebook"), _W/2-genfont2.getBounds(String.valueOf("Mtear Games on Facebook")).width/2, 2*(_H/3)-4*_H/15);
					genfont2.draw(_batch, String.valueOf("twitter.com/mteargames"), _W/2-genfont2.getBounds(String.valueOf("twitter.com/mteargames")).width/2, 2*(_H/3)-5*_H/15);
				}
			}
		}
	}

	@Override
	protected void update() {
		if(scoreanimating){
			animatescoretimer++;
			stextx += stextvx;
			stexty += stextvy;
			if(animatescoretimer > maxscoreframe){
				animatescoretimer=0;
				int nc = checkScore(gameboard[scoreanimatepoints.get(0).x][scoreanimatepoints.get(0).y]);
				totalholdingscore += nc;
				scoreanimatepoints.remove(0);
				if(scoreanimatepoints.size()==0){
					scoreanimating=false;
				}else{
					if(soundmode > 0) scorechime.play();
					stextx = gridx[scoreanimatepoints.get(0).x];
					stexty = gridy[scoreanimatepoints.get(0).y];
					stextvx = -1*(stextx-_W/2)/maxscoreframe;
					stextvy = (10*(_H/12)-stexty)/maxscoreframe;
					rotatepoints.add(scoreanimatepoints.get(0));
				}
			}
		}
		if(rotatepoints.size() > 0){
			rotateframetimer++;
			if(rotateframetimer > 1){
				rotateframetimer = 0;
				rotatecurrentframe++;
				if(rotatecurrentframe == 12){
					if(queuejump){queuejump=false;jumping=true;}
					rotatecurrentframe = 0;
					rotateframetimer = 0;
					rotatepoints.clear();
					if(darkrotating){
						darkrotating=false;
						populateScoreAnimate();
					}
				}
			}
		}
		if(flyingin){
			flytimer++;
			if(flydir==0){
				flyposy += flyyv;
			}else if(flydir==1){
				flyposy += flyyv2;
			}else if(flydir==2){
				flyposx -= flyxv2;
			}else if(flydir==3){
				flyposx += flyxv;
			}
			if(flytimer > flytotaltime){
				flyingin = false;
				flytimer=0;
			}
		}
		if(jumping){
			jumptimer++;
			if(jumptimer > maxjump){
				jumptimer = 0;
				jumping = false;
			}
		}
		if(isbuzzing){
			buzztimer++;
			if(buzztimer > maxbuzz){
				isbuzzing = false;
				buzztimer = 0;
			}
		}
		if(swipeguard){
			swipeguardtimer++;
			if(swipeguardtimer > 75){
				swipeguard = false;
				swipeguardtimer = 0;
			}
		}
		if(duringintrotimer && GAMESTATE != SCOREVIEW){
			introanimtimer++;
			treyx += treyboardvx;
			boardx += treyboardvx;
			if(resetting)boardx += treyboardvx;
			if(introanimtimer > maxintroframes){
				introanimtimer = 0;
				swipeguard = false;
				duringintrotimer = false;
				
				if(!completedtutorial){
					GAMESTATE = TUTORIAL;
					gameboard[1][1] = 1;
					tutorialswipes = 1;
					pastcontrolledtutorial = false;
				}else{
					GAMESTATE = PLAY;
				}
			}
		}
		if(duringintrotimer && GAMESTATE == SCOREVIEW && !resetting){
			introanimtimer++;
			treyx += treyboardvx;
			boardx += treyboardvx;
			if(introanimtimer > maxintroframes){
				introanimtimer = 0;
				swipeguard = false;
				duringintrotimer = false;
				preparescoreview = false;
			}
		}
		if(resetting){
			resettimer++;
			treyx += treyboardvx;
			if(resettimer > maxintroframes){
				swipeguard = false;
				resettimer = 0;
				resetting = false;
				GAMESTATE = MENU;
				treyx = 0;
			}
		}
	}
	
	@Override
	protected void destroy(){		
		genfont.dispose();
		genfont2.dispose();
		rotateatlas.dispose();
		drotateatlas.dispose();
		swipesound.dispose();
		alerttone.dispose();
		combinesound.dispose();
		newksound.dispose();
		bgmusic.dispose();
		scorechime.dispose();
		sound3000.dispose();
		sound6144.dispose();
	}
	
	String generateBoardString(){
		String board = "";
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				board += String.valueOf(gameboard[i][a]) + ";";
			}
			if(i < 3) board += "~";
		}
		return board;
	}
	
	int[][] generateBoardFromString(String s){
		int[][] ret = new int[4][4];
		String[] chunks1 = s.split("~");
		String[] chunks2 = null;
		for(int a = 0; a < chunks1.length; a++){
			chunks2 = chunks1[a].split(";");
			for(int i = 0; i < chunks2.length; i++){
				ret[a][i] = Integer.parseInt(chunks2[i]);
			}
		}
		return ret;
	}
	
	public void populateScoreAnimate(){
		for(int i = 3; i >= 0; i--){
			for(int a = 0; a < 4; a++){
				if(gameboard[a][i] > 2) scoreanimatepoints.add(new Point(a,i));
			}
		}
		scoreanimating = true;
		totalholdingscore=0;
		if(scoreanimatepoints.size()>0){
			if(soundmode > 0)scorechime.play();
			stextx = gridx[scoreanimatepoints.get(0).x];
			stexty = gridy[scoreanimatepoints.get(0).y];
			stextvx = -1*(stextx-_W/2)/maxscoreframe;
			stextvy = (10*(_H/12)-stexty)/maxscoreframe;
			rotatepoints.add(scoreanimatepoints.get(0));
		}
	}
	
	public void randomizeBoard(){
		zerocooldown = 25;
		scoreanimating = false;
		totalholdingscore=0;
		gameboard = clearBoard(gameboard);
		highestk = 0;
		int twos = 2, ones = 2;
		int rc = 9; Random r = new Random();
		nexttype = r.nextInt(3)+1;
		while(rc > 0){
			int rx = r.nextInt(4);
			int ry = r.nextInt(4);
			if(gameboard[rx][ry] < 0){
				rc--;
				int re = r.nextInt(3) + 1;
				if(twos > 0){ re = 2; twos--;}
				else if(ones > 0){ re = 1; ones--;}
				gameboard[rx][ry] = re;
			}
		}
	}
	
	public void readyImages(){
		float tx = _W*(3f/18f), ty = _H*(1f/8f);
		
		helpicon = new NBitmap("helpicon", tx*.75f, ty*.75f);
		nosound = new NBitmap("nosound", tx*.5f, ty*.5f);
		sound1 = new NBitmap("sound1", tx*.5f, ty*.5f);
		sound2 = new NBitmap("sound2", tx*.5f, ty*.5f);
		trophy = new NBitmap("trophy", tx*.5f, ty*.5f);
		
		rotateatlas = new TextureAtlas(Gdx.files.internal("data/rotatesheet.atlas"));
		AtlasRegion region = rotateatlas.findRegion("rotate0");
		rotateanimation = new NBitmap(region, tx, ty+(_H/100));
		
		drotateatlas = new TextureAtlas(Gdx.files.internal("data/drotatesheet.atlas"));
		region = drotateatlas.findRegion("drotate0");
		drotateanimation = new NBitmap(region, tx, ty+(_H/100));
		
		treyimage = new NBitmap("trey", 3*(_W/4), _H/3);
		
		gboard = new NBitmap("board", 5*(_W/6), 2*(_H/3));
		next1 = new NBitmap("next1", _W/6, _H/5.5f);
		next2 = new NBitmap("next2", _W/6, _H/5.5f);
		next3 = new NBitmap("next3", _W/6, _H/5.5f);
		next0 = new NBitmap("next0", _W/6, _H/5.5f);
		nextorange = new NBitmap("nextorange", _W/6, _H/5.5f);
		
		one = new NBitmap("1", tx, ty);
		two = new NBitmap("2", tx, ty);
		three = new NBitmap("3", tx, ty);
		six = new NBitmap("6", tx, ty);
		twelve = new NBitmap("12", tx, ty);
		twentyfour = new NBitmap("24", tx, ty);
		fourtyeight = new NBitmap("48", tx, ty);
		ninetysix = new NBitmap("96", tx, ty);
		n192 = new NBitmap("192", tx, ty);
		n384 = new NBitmap("384", tx, ty);
		n768 = new NBitmap("768", tx, ty);
		n1536 = new NBitmap("1536", tx, ty);
		n3072 = new NBitmap("3072", tx, ty);
		n6144 = new NBitmap("6144", tx, ty);
		redcard = new NBitmap("redcard", tx, ty);
		orangecard = new NBitmap("orangecard", tx, ty);
		
		sixp = new NBitmap("6pink", tx, ty);
		twelvep = new NBitmap("12pink", tx, ty);
		twentyfourp = new NBitmap("24pink", tx, ty);
		fourtyeightp = new NBitmap("48pink", tx, ty);
		ninetysixp = new NBitmap("96pink", tx, ty);
		n192p = new NBitmap("192pink", tx, ty);
		n384p = new NBitmap("384pink", tx, ty);
		n768p = new NBitmap("768pink", tx, ty);
		n1536p = new NBitmap("1536pink", tx, ty);
		n3072p = new NBitmap("3072pink", tx, ty);
		n6144p = new NBitmap("6144pink", tx, ty);
		
		onegrey = new NBitmap("1grey", tx, ty);
		twogrey = new NBitmap("2grey", tx, ty);
		redcardgrey = new NBitmap("redcardgrey", tx, ty);
		
		imagemap.put(0, redcard);
		imagemap.put(1, one);
		imagemap.put(2, two);
		imagemap.put(3, three);
		imagemap.put(6, six);
		imagemap.put(12, twelve);
		imagemap.put(24, twentyfour);
		imagemap.put(48, fourtyeight);
		imagemap.put(96, ninetysix);
		imagemap.put(192, n192);
		imagemap.put(384, n384);
		imagemap.put(768, n768);
		imagemap.put(1536, n1536);
		imagemap.put(3072, n3072);
		imagemap.put(6144, n6144);
		
		imagemap.put(10000 + 6, sixp);
		imagemap.put(10000 + 12, twelvep);
		imagemap.put(10000 + 24, twentyfourp);
		imagemap.put(10000 + 48, fourtyeightp);
		imagemap.put(10000 + 96, ninetysixp);
		imagemap.put(10000 + 192, n192p);
		imagemap.put(10000 + 384, n384p);
		imagemap.put(10000 + 768, n768p);
		imagemap.put(10000 + 1536, n1536p);
		imagemap.put(10000 + 3072, n3072p);
		imagemap.put(10000 + 6144, n6144p);
		
		
		imagemap.put(601, next1);
		imagemap.put(602, next2);
		imagemap.put(603, next3);
		imagemap.put(600, next0);
		imagemap.put(610, nextorange);
	}
	
	public int[][] clearBoard(int[][] b){
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				b[i][a] = -1;
			}
		}
		return b;
	}
	
	float getbuzzx(){
		int sign = 1;
		if(buzztimer % 2 == 0) sign = -1;
		return (_W/180)*sign;
	}
	
	float getjumpy(){
		float ret = 0;
		int sign = -1;
		float n = jumptimer;
		if(n > maxjump/2){ //first or second jump
			n /= 2;
		}
		if(n > maxjump/4){ //up or down
			sign = 1;
		}
		n /= 2;
		ret = n*(_H/150)*sign;
		return ret;
	}
	
	int[][] swipeDOWN(boolean ar){
		int[][] ret = copyArray(gameboard);
		//Start with second row, try to go down
		for(int a = 1; a < 4; a++){
			for(int i = 0; i < 4; i++){
				if(ret[i][a-1] < 0){ //move down if empty
					ret[i][a-1] = ret[i][a]; ret[i][a] = -1;
				}else if(ret[i][a] == 1){ //if it is a 1...
					if(ret[i][a-1] == 2 || ret[i][a-1] == 0){ //...combine with 2
						ret[i][a-1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a-1));
						if(ar){
							if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] == 2){ //if it is a 2...
					if(ret[i][a-1] == 1 || ret[i][a-1] == 0){ //...combine with 1
						ret[i][a-1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a-1));
						if(ar){
							if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] > 0 && ret[i][a] != 6144){ //otherwise look for a match
					if(ret[i][a-1] == ret[i][a] || ret[i][a-1] == 0){
						if(ret[i][a-1] == 0 && superpower == 1) ret[i][a-1] = 3*npow(2, highestk); else
						ret[i][a-1] = ret[i][a]*2; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a-1));
						if(ar)combine(ret[i][a-1]);
						if(ar){
							if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] == 0){ //if it is RED...
					if(ret[i][a-1] == 2 || ret[i][a-1] == 0){ //...combine with 2
						ret[i][a-1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a-1));
						if(ar){
							if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else if(ret[i][a-1] == 1 || ret[i][a-1] == 0){ //...combine with 1
						ret[i][a-1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a-1));
						if(ar){
							if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else{
						if(ret[i][a-1] != 6144){
							if(superpower == 1) ret[i][a-1] = 3*npow(2, highestk); else
							ret[i][a-1] = ret[i][a-1]*2; ret[i][a] = -1; //others
							if(ar)rotatepoints.add(new Point(i,a-1));
							if(ar)combine(ret[i][a-1]);
							if(ar){
								if(ret[i][a-1] == 3072 && soundmode > 0) sound3000.play();
								else if(ret[i][a-1]==6144 && soundmode > 0)sound6144.play();
								else if(soundmode > 0)combinesound.play();
							}
						}
					}
				}
			}
		}
		return ret;
	}
	
	int[][] swipeUP(boolean ar){
		int[][] ret = copyArray(gameboard);
		//Start with third row, try to go up
		for(int a = 2; a >= 0; a--){
			for(int i = 0; i < 4; i++){
				if(ret[i][a+1] < 0){ //move up if empty
					ret[i][a+1] = ret[i][a]; ret[i][a] = -1;
				}else if(ret[i][a] == 1){ //if it is a 1...
					if(ret[i][a+1] == 2 || ret[i][a+1] == 0){ //...combine with 2
						ret[i][a+1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a+1));
						if(ar){
							if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] == 2){ //if it is a 2...
					if(ret[i][a+1] == 1 || ret[i][a+1] == 0){ //...combine with 1
						ret[i][a+1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a+1));
						if(ar){
							if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] > 0  && ret[i][a] != 6144){ //otherwise look for a match
					if(ret[i][a+1] == ret[i][a] || ret[i][a+1] == 0){
						if(ret[i][a+1] == 0 && superpower == 1) ret[i][a+1] = 3*npow(2, highestk); else
						ret[i][a+1] = ret[i][a]*2; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a+1));
						if(ar)combine(ret[i][a+1]);
						if(ar){
							if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[i][a] == 0){ //if it is RED...
					if(ret[i][a+1] == 2 || ret[i][a+1] == 0){ //...combine with 2
						ret[i][a+1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a+1));
						if(ar){
							if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else if(ret[i][a+1] == 1 || ret[i][a+1] == 0){ //...combine with 1
						ret[i][a+1] = 3; ret[i][a] = -1;
						if(ar)rotatepoints.add(new Point(i,a+1));
						if(ar){
							if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else{
						if(ret[i][a+1] != 6144){
							if(superpower == 1) ret[i][a+1] = 3*npow(2, highestk); else
							ret[i][a+1] = ret[i][a+1]*2; ret[i][a] = -1; //others
							if(ar)rotatepoints.add(new Point(i,a+1));
							if(ar)combine(ret[i][a+1]);
							if(ar){
								if(ret[i][a+1] == 3072 && soundmode > 0) sound3000.play();
								else if(ret[i][a+1]==6144 && soundmode > 0)sound6144.play();
								else if(soundmode > 0)combinesound.play();
							}
						}
					}
				}
			}
		}
		return ret;
	}
	
	int[][] swipeRIGHT(boolean ar){
		int[][] ret = copyArray(gameboard);
		//Start with third column, try to go right
		for(int a = 2; a >= 0; a--){
			for(int i = 0; i < 4; i++){
				if(ret[a+1][i] < 0){ //move up if empty
					ret[a+1][i] = ret[a][i]; ret[a][i] = -1;
				}else if(ret[a][i] == 1){ //if it is a 1...
					if(ret[a+1][i] == 2 || ret[a+1][i] == 0){ //...combine with 2
						ret[a+1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a+1,i));
						if(ar){
							if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] == 2){ //if it is a 2...
					if(ret[a+1][i] == 1 || ret[a+1][i] == 0){ //...combine with 1
						ret[a+1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a+1,i));
						if(ar){
							if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] > 0 && ret[a][i] != 6144){ //otherwise look for a match
					if(ret[a+1][i] == ret[a][i] || ret[a+1][i] == 0){
						if(ret[a+1][i] == 0 && superpower == 1) ret[a+1][i] = 3*npow(2, highestk); else
						ret[a+1][i] = ret[a][i]*2; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a+1,i));
						if(ar)combine(ret[a+1][i]);
						if(ar){
							if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] == 0){ //if it is RED...
					if(ret[a+1][i] == 2 || ret[a+1][i] == 0){ //...combine with 2
						ret[a+1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a+1,i));
						if(ar){
							if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else if(ret[a+1][i] == 1 || ret[a+1][i] == 0){ //...combine with 1
						ret[a+1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a+1,i));
						if(ar){
							if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else{
						if(ret[a+1][i] != 6144){
							if(superpower == 1) ret[a+1][i] = 3*npow(2, highestk); else
							ret[a+1][i] = ret[a+1][i]*2; ret[a][i] = -1; //others
							if(ar)rotatepoints.add(new Point(a+1,i));
							if(ar)combine(ret[a+1][i]);
							if(ar){
								if(ret[a+1][i] == 3072 && soundmode > 0) sound3000.play();
								else if(ret[a+1][i]==6144 && soundmode > 0)sound6144.play();
								else if(soundmode > 0)combinesound.play();
							}
						}
					}
				}
			}
		}
		return ret;
	}
	
	int[][] swipeLEFT(boolean ar){
		int[][] ret = copyArray(gameboard);
		//Start with second column, try to go left
		for(int a = 1; a < 4; a++){
			for(int i = 0; i < 4; i++){
				if(ret[a-1][i] < 0){ //move up if empty
					ret[a-1][i] = ret[a][i]; ret[a][i] = -1;
				}else if(ret[a][i] == 1){ //if it is a 1...
					if(ret[a-1][i] == 2 || ret[a-1][i] == 0){ //...combine with 2
						ret[a-1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a-1,i));
						if(ar){
							if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] == 2){ //if it is a 2...
					if(ret[a-1][i] == 1 || ret[a-1][i] == 0){ //...combine with 1
						ret[a-1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a-1,i));
						if(ar){
							if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] > 0 && ret[a][i] != 6144){ //otherwise look for a match
					if(ret[a-1][i] == ret[a][i] || ret[a-1][i] == 0){
						if(ret[a-1][i] == 0 && superpower == 1) ret[a-1][i] = 3*npow(2, highestk); else
						ret[a-1][i] = ret[a][i]*2; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a-1,i));
						if(ar)combine(ret[a-1][i]);
						if(ar){
							if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}
				}else if(ret[a][i] == 0){ //if it is RED...
					if(ret[a-1][i] == 2 || ret[a-1][i] == 0){ //...combine with 2
						ret[a-1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a-1,i));
						if(ar){
							if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else if(ret[a-1][i] == 1 || ret[a-1][i] == 0){ //...combine with 1
						ret[a-1][i] = 3; ret[a][i] = -1;
						if(ar)rotatepoints.add(new Point(a-1,i));
						if(ar){
							if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
							else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
							else if(soundmode > 0)combinesound.play();
						}
					}else{
						if(ret[a-1][i] != 6144){
							if(superpower == 1) ret[a-1][i] = 3*npow(2, highestk); else
							ret[a-1][i] = ret[a-1][i]*2; ret[a][i] = -1; //others
							if(ar)rotatepoints.add(new Point(a-1,i));
							if(ar)combine(ret[a-1][i]);
							if(ar){
								if(ret[a-1][i] == 3072 && soundmode > 0) sound3000.play();
								else if(ret[a-1][i]==6144 && soundmode > 0)sound6144.play();
								else if(soundmode > 0)combinesound.play();
							}
						}
					}
				}
			}
		}
		return ret;
	}
	
	boolean areSame(int[][] ray1, int[][] ray2){
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				if(ray1[i][a] != ray2[i][a]) return false;
			}
		}
		return true;
	}
	
	int[][] copyArray(int[][] ray){
		int[][] ret = new int[4][4];
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				ret[i][a] = ray[i][a];
			}
		}
		return ret;
	}
	
	void postTurn(int dir){
		if(zerocooldown > 0) zerocooldown--;
		if(soundmode > 0)swipesound.play();
		boolean madeobject = false;
		Random r = new Random();
		int mx=0,my=0;
		if(nexttype >= 0){
			if(highestk > 3 && nexttype == 3 && r.nextInt(2) == 1){
				int newk = r.nextInt(highestk-2);
				nexttype = (int) (npow(2, newk) * 3);
			}
			while(!madeobject){
				int ploc = r.nextInt(4);
				switch(dir){
				case 0: {if(gameboard[ploc][0] == -1){ gameboard[ploc][0] = nexttype; mx=ploc;my=0; madeobject=true;} break;}
				case 1: {if(gameboard[ploc][3] == -1){ gameboard[ploc][3] = nexttype; mx=ploc;my=3; madeobject=true;} break;}
				case 2: {if(gameboard[3][ploc] == -1){ gameboard[3][ploc] = nexttype; mx=3;my=ploc; madeobject=true;} break;}
				case 3: {if(gameboard[0][ploc] == -1){ gameboard[0][ploc] = nexttype; mx=0;my=ploc; madeobject=true;} break;}
				}
			}
			startflying(dir, mx, my, nexttype);
			
			if(GAMESTATE == TUTORIAL)nexttype = -1;
		}
		if(GAMESTATE != TUTORIAL || pastcontrolledtutorial){
			boolean gc = false;
			while(!gc){
				int cc = r.nextInt(3) + 1;
				if(r.nextInt(25)==0 && zerocooldown == 0 && countnum(0)==0){superpower=r.nextInt(2); cc=0; zerocooldown = 30; gc=true;}
				if(cc == 1 && countnum(1) > 3) cc = r.nextInt(3) + 1;
				if(cc == 2 && countnum(2) > 3) cc = r.nextInt(3) + 1;
				if(countnum(2) > 3 && countnum(1) == 0 && r.nextInt(2) == 0) cc = 1;
				if(countnum(1) > 3 && countnum(2) == 0 && r.nextInt(2) == 0) cc = 2;
				if(cc == nexttype){
					if(hadthistype < ((nexttype == 3)?5:2)){
						hadthistype++;
						nexttype = cc;
						gc = true;
					}
				}else{
					hadthistype = 0;
					nexttype = cc;
					gc = true;
				}
			}
		}
		if(GAMESTATE == TUTORIAL){ //tutorial
			tutorialtimer++;
			if(currentmessage < 2){
				if(currentmessage == 0 && tutorialtimer == tutorialswipes)nexttype = 1;
				if(currentmessage == 1 && tutorialtimer == tutorialswipes){
					nexttype = 2;
				}
				if(tutorialtimer > tutorialswipes){
					tutorialtimer = 0;
					currentmessage++;
					tutorialswipes = 2;
				}
			}else{
				if(currentmessage == 7){
					if(tutorialtimer > tutorialswipes){
						pastcontrolledtutorial = false;
						tutorialtimer = 0;
						GAMESTATE = PLAY;
						completedtutorial = true;
						zerocooldown=20;
						nexttype = 1;
					}
				}
				if(currentmessage == 2){
					if(countnum(3) == 1){
						currentmessage++;
						nexttype = 3;
						tutorialtimer=0;
					}
				}
				if(currentmessage == 3){
					if(countnum(6) == 1){
						currentmessage++;
						nexttype = 1;
						tutorialtimer=0;
						tutorialswipes = 5;
					}
					if(tutorialtimer == 5 && countnum (3) < 3){
						nexttype = 3;
					}
				}
				if(currentmessage == 4 && tutorialtimer == tutorialswipes){
					currentmessage++;
					nexttype = 0;
					tutorialtimer = 0;
				}
				if(currentmessage == 5 && countnum(0) == 0 && countnum(6) < 2)nexttype = 0;
				if(currentmessage == 5 && countnum(0) == 0 && countnum(6) <= 2 && tutorialtimer > 5)nexttype = 0;
				
				if(currentmessage == 5 && countnum(12) > 0 && tutorialtimer > 1 && countnum(0) == 0 || (currentmessage == 5 && countnum(24) > 0)){
					currentmessage++;
					tutorialtimer = 0;
					tutorialswipes = 4;
					nexttype = 0; superpower = 1;
				}
				if(currentmessage == 6){
					if(tutorialtimer > 1 && countnum(0) == 0 && countnum(24) < 1)nexttype = 0;
					if(countnum(0) == 0 && countnum(24) > 0 && tutorialtimer > 1){
						currentmessage++;
						tutorialtimer = 0;
						superpower = 0;
						pastcontrolledtutorial = true;
						nexttype = 1;
						tutorialswipes = 5;
					}
				}
				
			}
		}
		
		//check death
		if(areSame(gameboard, swipeUP(false)) && areSame(gameboard, swipeDOWN(false)) && areSame(gameboard, swipeLEFT(false)) && areSame(gameboard, swipeRIGHT(false))){
			die();
		}
	}
	
	void die(){
		highestk = 0;
		tempscore = totalScore();
		alive = false;
		GAMESTATE = GAMEOVER1;
		swipeguard = true;
		int score = totalScore();
		if(score > highscores[0]) this.highestboardstring = generateBoardString();
		for(int i = 0; i < highscores.length; i++){
			if(score > highscores[i]){
				int b = 0, c = highscores[i];
				for(int a = i; a < highscores.length-1; a++){
					b = highscores[a+1];
					highscores[a+1] = c;
					c = b;
				}
				highscores[i] = score;
				break;
			}
		}
		pause();
	}
	
	void popdarkrotate(){
		darkrotating = true;
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				if(gameboard[i][a] == 1 || gameboard[i][a] == 2 || gameboard[i][a] == 0)
					rotatepoints.add(new Point(i,a));
			}
		}
	}
	
	void startflying(int dir, int i, int a, int n){
		flyingin=true;
		flyx=i;flyy=a;
		flydir=dir;
		flysprite = imagemap.get(n);
		if(n == 0 && superpower == 1) flysprite = orangecard;
		
		if(dir==0){
			flyposx = gridx[i];flyposy=0-flysprite.getHeight();
		}else if(dir==1){
			flyposx = gridx[i];flyposy=_H+flysprite.getHeight();
		}else if(dir==2){
			flyposx = _W;flyposy=gridy[a];
		}else if(dir==3){
			flyposx = 0-flysprite.getWidth();flyposy=gridy[a];
		}
	}
	
	int totalScore(){
		int score = 0;
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				score += checkScore(gameboard[i][a]);
			}
		}
		return score;
	}
	
	int checkScore(int v){
		if(v <= 2) return 0;
		int k = (int) (Math.log(v/3)/Math.log(2));
		return  npow(3, k+1);
	}
	
	int npow(int b, int e){
		if(e <= 0) return 1;
		if(e == 1) return b;
		else return b * npow(b, e-1);
	}
	
	float getDrawX(int i, int a){
		float drawx = 0;
		if(swiping){ //if swiping
			if(xswipe){ //if swiping on x axis
				if(offsetx > 0){ //swiping right
					if(i < 3){
						drawx += offsetx;
						if(!merges2(swipeRIGHT(false),i,a,i+1,a)) return 0;
					}
				}else{ //swiping left
					if(i > 0){
						drawx += offsetx;
						if(!merges2(swipeLEFT(false),i,a,i-1,a)) return 0;
					}
				}
			}
		}
		return drawx;
	}
	
	float getDrawY(int i, int a){
		float drawy = 0;
		if(swiping){ //if swiping
			if(!xswipe){ //if swiping on y axis
				if(offsety > 0){ //swiping up
					if(a < 3){
						drawy += offsety;
						if(!merges2(swipeUP(false),i,a,i,a+1)) return 0;
					}
				}else{ //swiping down
					if(a > 0){
						if(!merges2(swipeDOWN(false),i,a,i,a-1)) return 0;
						drawy += offsety;
					}
				}
			}
		}
		return drawy;
	}
	
	boolean merges2(int[][] ray, int i1, int a1, int i2, int a2){
		return (merges(ray, i1,a1,i2,a2) || merges(gameboard,i1,a1,i2,a2));
	}
	
	boolean merges(int[][] ray, int i1, int a1, int i2, int a2){
		if(ray[i1][a1] == 1){
			if(ray[i2][a2] == 2 || ray[i2][a2] <= 0) return true; else return false;
		}else if(ray[i1][a1] == 2){
			if(ray[i2][a2] == 1 || ray[i2][a2] <= 0) return true; else return false;
		}else if(ray[i1][a1] <= 0){
			return true;
		}else{
			if(ray[i1][a1] == 6144 || ray[i2][a2] == 6144)return false;
			if(ray[i1][a1] == ray[i2][a2] || ray[i2][a2] <= 0) return true; else return false;
		}
	}
	
	boolean isrotatingat(int i, int a){
		for(Point p : rotatepoints){
			if(p.x == i && p.y == a) return true;
		}
		return false;
	}
	
	int countnum(int n){
		int r = 0;
		for(int i = 0; i < 4; i++){
			for(int a = 0; a < 4; a++){
				if(gameboard[i][a] == n)r++;
			}
		}
		return r;
	}
	
	int getK(int n){
		int k = (int) (Math.log(n/3)/Math.log(2));
		return k;
	}

	void combine(int n){
		int k = getK(n);
		if(k > highestk){
			queuejump = true;
			highestk = k;
			jumping = true;
			if(soundmode > 0)newksound.play();
		}else if(k == highestk){
			queuejump = true;
		}
	}
	void stopanimations(){
		rotatepoints.clear(); rotateframetimer = 0; rotatecurrentframe = 0;
		jumping = false; queuejump = false; jumptimer = 0;
		flyingin = false;
		isbuzzing = false; buzztimer = 0;
	}
	
	void startplaying(){
		if(!duringintrotimer)
			GAMESTATE = PLAY;
		if(!alive){
			alive = true;
			if(completedtutorial)
				randomizeBoard();
		}
	}
	
	void reset(){
		swipeguard = true;
		resetting = true;
		resettimer = 0;
		GAMESTATE = RESETTING;
	}
	
	@Override
	public void pause() {
		//save settings
		_savesettings.putInteger("soundmode", soundmode);
		_savesettings.putBoolean("completedtutorial", this.completedtutorial);
		_savesettings.putInteger("highscore0", highscores[0]);
		_savesettings.putInteger("highscore1", highscores[1]);
		_savesettings.putInteger("highscore2", highscores[2]);
		_savesettings.putInteger("highscore3", highscores[3]);
		_savesettings.putInteger("highscore4", highscores[4]);
		_savesettings.putString("highestboardstring", highestboardstring);
		_savesettings.flush();

	}
	
	///////////////////////////////////////////////////////
	// TOUCH EVENTS
	///////////////////////////////////////////////////////

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if(scoreanimating || darkrotating)return true;
		offsetxstart = x;
		offsetystart = y;
		stopanimations();
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		y = _H - y;
		if(scoreanimating){
			scoreanimating = false;
			totalholdingscore = totalScore();
			scoreanimatepoints.clear();
			rotatepoints.clear();
			animatescoretimer = 0;
			return true;
		}
		if(x >= _W-nosound.getWidth()-helpicon.getWidth()/4 && x <= _W-nosound.getWidth()-helpicon.getWidth()/4+nosound.getWidth()
				&& y >= helpicon.getHeight()/4+nosound.getHeight()/5 && y <= helpicon.getHeight()/4+nosound.getHeight()/5 + nosound.getHeight()){
			//sound
			soundmode++;
			if(soundmode == 3){
				soundmode = 0;
				bgmusic.stop();
			}else if(soundmode == 1){
				
			}else if(soundmode == 2){
				bgmusic.play();
			}
		}
		if(GAMESTATE == MENU && completedtutorial && !scoreanimating){
			if(x >= helpicon.getWidth()/4 && x <= helpicon.getWidth()/4+helpicon.getWidth()
					&& y >= helpicon.getHeight()/4 && y <= helpicon.getHeight()/4+helpicon.getHeight()){
				//help
				clearBoard(gameboard);
				completedtutorial = false;
				currentmessage = 0;
				tutorialtimer = 0;
				scorestate = 0;
				fling(500, 0, 0);
			}
			if(x >= _W/2-trophy.getWidth()/2 && x <= _W/2-trophy.getWidth()/2+trophy.getWidth()
					&& y >= helpicon.getHeight()/4 && y <= helpicon.getHeight()/4+helpicon.getHeight()){
				//high scores
				GAMESTATE = this.SCOREVIEW;
				gameboard = this.generateBoardFromString(this.highestboardstring);
				
				int velocityX = new Random().nextInt(4) - new Random().nextInt(4); if(velocityX == 0)velocityX = 1;
				if(velocityX < 0){
					boardx = _W;
					treyboardvx = -1*(_W-(_W/2-gboard.getWidth()/2))/maxintroframes;
				}else{
					boardx = 0-gboard.getWidth();
					treyboardvx = (gboard.getWidth()+(_W/2-gboard.getWidth()/2))/maxintroframes;
				}
				introanimtimer = 0;
				duringintrotimer = true;
				swipeguard = true;
				this.preparescoreview = true;
				return true;
			}
		}
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {  return false;	}
	
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(scoreanimating)return true;
		if(swipeguard)return true;
		if(GAMESTATE == GAMEOVER1){
			popdarkrotate();
			GAMESTATE = GAMEOVER2;
			return true;
		}else if(GAMESTATE == GAMEOVER2){
			if(Math.abs(velocityY) > Math.abs(velocityX))return true;
			if(velocityX < 0){
				treyx = _W-(_W/2-treyimage.getWidth()/2);
				treyboardvx = -1*(treyx)/maxintroframes;
			}else{
				treyx = 0-treyimage.getWidth()-(_W/2-treyimage.getWidth()/2);
				treyboardvx = -1*(treyx)/maxintroframes;
			}
			reset();
			return true;
		}
		if(GAMESTATE == MENU){
			if(Math.abs(velocityY) > Math.abs(velocityX))return true;
			treyx = _W/2-treyimage.getWidth()/2;
			if(velocityX < 0){
				boardx = _W;
				treyboardvx = -1*(_W-(_W/2-gboard.getWidth()/2))/maxintroframes;
			}else{
				boardx = 0-gboard.getWidth();
				treyboardvx = (gboard.getWidth()+(_W/2-gboard.getWidth()/2))/maxintroframes;
			}
			introanimtimer = 0;
			duringintrotimer = true;
			swipeguard = true;
			startplaying();
			return true;
		}
		if(GAMESTATE == SCOREVIEW){
			if(Math.abs(velocityY) > Math.abs(velocityX))return true;
			if(velocityX < 0){
				scorestate++;
				if(scorestate > 2) scorestate = 2;
				/*if(scorestate == 1){
					boardx = 0-gboard.getWidth();
					treyboardvx = (gboard.getWidth()+(_W/2-gboard.getWidth()/2))/maxintroframes;
					introanimtimer = 0;
					duringintrotimer = true;
					swipeguard = true;
					return true;
				}*/
			}else{
			scorestate--;
			if(scorestate == -1){
					scorestate = 0;
					if(Math.abs(velocityY) > Math.abs(velocityX))return true;
					treyx = 0-treyimage.getWidth()-(_W/2-treyimage.getWidth()/2);
					treyboardvx = -1*(treyx)/maxintroframes; treyboardvx /=2;
					reset();
					introanimtimer = 0;
					duringintrotimer = true;
					swipeguard = true;
					return true;
			}
			if(scorestate == 0){
				if(Math.abs(velocityY) > Math.abs(velocityX))return true;
				treyx = _W/2-treyimage.getWidth()/2;
				boardx = 0-gboard.getWidth();
				treyboardvx = (gboard.getWidth()+(_W/2-gboard.getWidth()/2))/maxintroframes;
				introanimtimer = 0;
				duringintrotimer = true;
				swipeguard = true;
				return true;
			}
			}
			return true;
		}
		
		if(Math.abs(velocityX) > Math.abs(velocityY)){ //fling x
			if(Math.abs(velocityX) < 250) return true;
			if(velocityX > 0){
				int[][] propose = swipeRIGHT(false);
				if(!areSame(propose, gameboard)){
					swipeRIGHT(true);
					gameboard = propose;
					postTurn(3);
				}else{
					isbuzzing=true;
					if(soundmode > 0)alerttone.play();
				}
			} else {
				int[][] propose = swipeLEFT(false);
				if(!areSame(propose, gameboard)){
					swipeLEFT(true);
					gameboard = propose;
					postTurn(2);
				}else{
					isbuzzing=true;
					if(soundmode > 0)alerttone.play();
				}
			}
		}else{//flingy
			if(Math.abs(velocityY) < 250) return true;
			if(velocityY > 0){
				int[][] propose = swipeDOWN(false);
				if(!areSame(propose, gameboard)){
					swipeDOWN(true);
					gameboard = propose;
					postTurn(1);
				}else{
					isbuzzing=true;
					if(soundmode > 0)alerttone.play();
				}
			}else {
				int[][] propose = swipeUP(false);
				if(!areSame(propose, gameboard)){
					swipeUP(true);
					gameboard = propose;
					postTurn(0);
				}else{
					isbuzzing=true;
					if(soundmode > 0)alerttone.play();
				}
			}
		}
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if(scoreanimating)return true;
		if(GAMESTATE != PLAY && GAMESTATE != TUTORIAL) return true;
		
		if(!swiping)
			xswipe = (Math.abs(deltaX) > Math.abs(deltaY));
		swiping = true;
		offsetx = x-offsetxstart;
		if(offsetx > 0)if(offsetx > _W/5.5f)offsetx = _W/5.5f;
		if(offsetx < 0)if(offsetx < -1*_W/5.5f)offsetx = -1*_W/5.5f;
		offsety = offsetystart-y;
		if(offsety > 0)if(offsety > _H/7)offsety = _H/7;
		if(offsety < 0)if(offsety < -1*_H/7)offsety = -1*_H/7;
		return true;	
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		offsetx = 0; offsetxstart = 0;
		offsety = 0; offsetystart = 0;
		swiping = false;
		return true;	
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {		return false;	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,Vector2 pointer1, Vector2 pointer2) {	return false;	}
	
	class Point{
		public int x, y;
		public Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
