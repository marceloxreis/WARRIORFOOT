export type DivisionTeams = {
  [divisionLevel: number]: TeamBasicInfo[];
};

export type LeagueInfo = {
  divisions: DivisionTeams;
};

export type TeamBasicInfo = {
  id: string;
  name: string;
  colorPrimary: string;
  colorSecondary: string;
  divisionLevel: number;
};
