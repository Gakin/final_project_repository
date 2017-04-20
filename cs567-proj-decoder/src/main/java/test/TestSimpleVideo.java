package test;

import cs567.decode.BaseDecoder;
import cs567.decode.SimpleDecoder;
import cs567.player.VideoPlayer;
import cs567.rep.simple.SimpleVideo;
import cs567.utils.VideoUtils;

public class TestSimpleVideo {
	public static void main(String[] args) {
		SimpleVideo video = VideoUtils.readSimpleVideo("c:\\cs576\\oneperson_960_540.rgb", 960, 540);
		BaseDecoder decoder = new SimpleDecoder();
		VideoPlayer vp = new VideoPlayer();
		vp.setDecoder(video, decoder, 30);
		vp.playVideo();
	}
}
