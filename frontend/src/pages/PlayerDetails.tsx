import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { playerApi } from '../api/playerApi';
import type { PlayerDetails } from '../types/player';

export default function PlayerDetailsPage() {
  const navigate = useNavigate();
  const { playerId } = useParams<{ playerId: string }>();
  const [player, setPlayer] = useState<PlayerDetails | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!playerId) return;

    playerApi
      .getPlayerDetails(playerId)
      .then(setPlayer)
      .catch(() => navigate(-1))
      .finally(() => setLoading(false));
  }, [playerId, navigate]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center">
        <p className="text-slate-400">Loading...</p>
      </div>
    );
  }

  if (!player) return null;

  const getStatColor = (value: number) => {
    if (value >= 80) return 'text-green-400';
    if (value >= 70) return 'text-blue-400';
    if (value >= 60) return 'text-yellow-400';
    if (value >= 50) return 'text-orange-400';
    return 'text-red-400';
  };

  const getBarColor = (value: number) => {
    if (value >= 80) return 'bg-green-500';
    if (value >= 70) return 'bg-blue-500';
    if (value >= 60) return 'bg-yellow-500';
    if (value >= 50) return 'bg-orange-500';
    return 'bg-red-500';
  };

  const StatBar = ({ label, value }: { label: string; value: number }) => (
    <div className="flex justify-between items-center py-2 border-b border-slate-700/30 last:border-0">
      <span className="text-sm text-slate-300">{label}</span>
      <span className={`text-lg font-bold ${getStatColor(value)}`}>{value}</span>
    </div>
  );

  const renderGKStats = () => (
    <>
      <StatSection title="Diving" stats={[
        { label: 'Diving', value: player.diving },
        { label: 'Handling', value: player.handling },
        { label: 'Kicking', value: player.kicking },
      ]} />
      <StatSection title="Reflexes" stats={[
        { label: 'Reflexes', value: player.reflexes },
        { label: 'Speed', value: player.speed },
        { label: 'Positioning', value: player.positioning },
      ]} />
      <StatSection title="Physical" stats={[
        { label: 'Jumping', value: player.jumping },
        { label: 'Strength', value: player.strength },
      ]} />
    </>
  );

  const renderDFStats = () => (
    <>
      <StatSection title="Defending" stats={[
        { label: 'Interceptions', value: player.interceptions },
        { label: 'Heading Acc.', value: player.headingAcc },
        { label: 'Def. Aware', value: player.defAware },
        { label: 'Stand Tackle', value: player.standTackle },
        { label: 'Slide Tackle', value: player.slideTackle },
      ]} />
      <StatSection title="Physical" stats={[
        { label: 'Jumping', value: player.jumping },
        { label: 'Stamina', value: player.stamina },
        { label: 'Strength', value: player.strength },
        { label: 'Aggression', value: player.aggression },
      ]} />
      <StatSection title="Pace" stats={[
        { label: 'Acceleration', value: player.acceleration },
        { label: 'Sprint Speed', value: player.sprintSpeed },
      ]} />
    </>
  );

  const renderMFStats = () => (
    <>
      <StatSection title="Passing" stats={[
        { label: 'Vision', value: player.vision },
        { label: 'Crossing', value: player.crossing },
        { label: 'FK Acc.', value: player.fkAcc },
        { label: 'Short Pass', value: player.shortPass },
        { label: 'Long Pass', value: player.longPass },
        { label: 'Curve', value: player.curve },
      ]} />
      <StatSection title="Dribbling" stats={[
        { label: 'Agility', value: player.agility },
        { label: 'Balance', value: player.balance },
        { label: 'Reactions', value: player.reactions },
        { label: 'Ball Control', value: player.ballControl },
        { label: 'Dribbling', value: player.dribblingSkill },
        { label: 'Composure', value: player.composure },
      ]} />
      <StatSection title="Physical" stats={[
        { label: 'Stamina', value: player.stamina },
        { label: 'Strength', value: player.strength },
      ]} />
    </>
  );

  const renderFWStats = () => (
    <>
      <StatSection title="Pace" stats={[
        { label: 'Acceleration', value: player.acceleration },
        { label: 'Sprint Speed', value: player.sprintSpeed },
      ]} />
      <StatSection title="Shooting" stats={[
        { label: 'Att. Position', value: player.attPosition },
        { label: 'Finishing', value: player.finishing },
        { label: 'Shot Power', value: player.shotPower },
        { label: 'Long Shots', value: player.longShots },
        { label: 'Volleys', value: player.volleys },
        { label: 'Penalties', value: player.penalties },
      ]} />
      <StatSection title="Dribbling" stats={[
        { label: 'Agility', value: player.agility },
        { label: 'Balance', value: player.balance },
        { label: 'Reactions', value: player.reactions },
        { label: 'Ball Control', value: player.ballControl },
        { label: 'Dribbling', value: player.dribblingSkill },
        { label: 'Composure', value: player.composure },
      ]} />
      <StatSection title="Physical" stats={[
        { label: 'Jumping', value: player.jumping },
        { label: 'Stamina', value: player.stamina },
        { label: 'Strength', value: player.strength },
        { label: 'Aggression', value: player.aggression },
      ]} />
    </>
  );

  const StatSection = ({ title, stats }: { title: string; stats: { label: string; value: number }[] }) => (
    <div className="bg-slate-800/40 backdrop-blur-sm rounded-xl p-6 border border-slate-700/50">
      <h3 className="text-lg font-bold text-white mb-4">{title}</h3>
      {stats.map((stat) => (
        <StatBar key={stat.label} label={stat.label} value={stat.value} />
      ))}
    </div>
  );

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
    }).format(value);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-8">
      <div className="max-w-7xl mx-auto">
        <button
          onClick={() => navigate(-1)}
          className="mb-6 px-6 py-2 bg-slate-700/50 backdrop-blur-sm text-white rounded-lg hover:bg-slate-600/50 transition-all"
        >
          Back
        </button>

        <div className="bg-slate-800/50 backdrop-blur-sm rounded-2xl p-8 border border-slate-700/50 mb-8">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="text-4xl font-bold text-white mb-2">{player.name}</h1>
              <div className="flex items-center gap-4 text-slate-400">
                <span>{player.age} years old</span>
                <span className="text-2xl">•</span>
                <span className="text-2xl font-bold text-blue-400">{player.position}</span>
              </div>
            </div>
            <div className="text-right">
              <div className="text-6xl font-black text-white mb-2">{player.overall}</div>
              <div className="text-sm text-slate-400">OVERALL</div>
            </div>
          </div>
          <div className="text-center py-4 bg-slate-900/40 rounded-lg">
            <div className="text-sm text-slate-400 mb-1">Market Value</div>
            <div className="text-2xl font-bold text-green-400">{formatCurrency(player.marketValue)}</div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {player.position === 'GK' && renderGKStats()}
          {player.position === 'DF' && renderDFStats()}
          {player.position === 'MF' && renderMFStats()}
          {player.position === 'FW' && renderFWStats()}
        </div>
      </div>
    </div>
  );
}
