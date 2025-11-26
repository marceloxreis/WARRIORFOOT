export type PlayerDetails = {
  id: string;
  name: string;
  age: number;
  position: string;
  overall: number;
  marketValue: number;

  pace: number | null;
  acceleration: number | null;
  sprintSpeed: number | null;

  shooting: number | null;
  attPosition: number | null;
  finishing: number | null;
  shotPower: number | null;
  longShots: number | null;
  volleys: number | null;
  penalties: number | null;

  passing: number | null;
  vision: number | null;
  crossing: number | null;
  fkAcc: number | null;
  shortPass: number | null;
  longPass: number | null;
  curve: number | null;

  dribbling: number | null;
  agility: number | null;
  balance: number | null;
  reactions: number | null;
  ballControl: number | null;
  dribblingSkill: number | null;
  composure: number | null;

  defending: number | null;
  interceptions: number | null;
  headingAcc: number | null;
  defAware: number | null;
  standTackle: number | null;
  slideTackle: number | null;

  physical: number | null;
  jumping: number | null;
  stamina: number | null;
  strength: number | null;
  aggression: number | null;

  diving: number | null;
  handling: number | null;
  kicking: number | null;
  reflexes: number | null;
  speed: number | null;
  positioning: number | null;
};
