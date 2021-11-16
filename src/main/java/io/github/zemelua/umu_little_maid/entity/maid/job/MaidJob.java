package io.github.zemelua.umu_little_maid.entity.maid.job;

public class MaidJob {
	private final Aggression aggression;
	private final boolean leap;

	protected MaidJob(Builder builder) {
		this.aggression = builder.aggression;
		this.leap = builder.leap;
	}

	public boolean isMad() {
		return this.aggression == Aggression.MAD;
	}

	public boolean isActive() {
		return this.aggression == Aggression.ACTIVE;
	}

	public boolean isGuard() {
		return this.aggression == Aggression.GUARD;
	}

	public boolean isAvoid() {
		return this.aggression == Aggression.AVOID;
	}

	public boolean canLeapAtTarget() {
		return this.leap;
	}

	public static class Builder {
		private Aggression aggression = Aggression.AVOID;
		private boolean leap = false;

		protected Builder setMad() {
			this.aggression = Aggression.MAD;

			return this;
		}

		protected Builder setActive() {
			this.aggression = Aggression.ACTIVE;

			return this;
		}

		protected Builder setGuard() {
			this.aggression = Aggression.GUARD;

			return this;
		}

		protected Builder setLeap() {
			this.leap = true;

			return this;
		}
	}

	private enum Aggression {
		MAD,
		ACTIVE,
		GUARD,
		AVOID
	}
}
