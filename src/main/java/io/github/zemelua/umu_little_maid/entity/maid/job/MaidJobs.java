package io.github.zemelua.umu_little_maid.entity.maid.job;

public final class MaidJobs {
	public static final MaidJob FENCER = new MaidJob(new MaidJob.Builder().setActive().setLeap());
	public static final MaidJob NONE = new MaidJob(new MaidJob.Builder());
}
