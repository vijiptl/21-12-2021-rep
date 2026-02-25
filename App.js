import React, { useMemo, useState } from 'react';
import {
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { StatusBar } from 'expo-status-bar';

const APP_CATALOG = [
  { id: 'chato', name: 'Chato', creditsPerDollar: 13 },
  { id: 'omar', name: 'Omar', creditsPerDollar: 11 },
  { id: 'livu', name: 'LivU', creditsPerDollar: 15 },
  { id: 'azar', name: 'Azar', creditsPerDollar: 10 },
];

const LEGIT_EARNING_OPTIONS = [
  { label: 'Daily login rewards', creditsPerDay: 20 },
  { label: 'Event participation', creditsPerDay: 35 },
  { label: 'Creator tasks / referrals', creditsPerDay: 45 },
  { label: 'Promo bonus codes', creditsPerDay: 15 },
];

export default function App() {
  const [selectedApp, setSelectedApp] = useState(APP_CATALOG[0]);
  const [budget, setBudget] = useState('20');

  const budgetValue = Number.parseFloat(budget) || 0;

  const projectedCredits = useMemo(
    () => Math.floor(budgetValue * selectedApp.creditsPerDollar),
    [budgetValue, selectedApp]
  );

  const earnedPerDay = LEGIT_EARNING_OPTIONS.reduce(
    (sum, item) => sum + item.creditsPerDay,
    0
  );

  const projectedMonthlyFreeCredits = earnedPerDay * 30;

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar style="light" />
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.heading}>Social App Credit Planner</Text>
        <Text style={styles.subheading}>
          This app helps estimate legitimate credit usage and reward opportunities. It does not generate or hack credits.
        </Text>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>1) Pick your app</Text>
          <View style={styles.appList}>
            {APP_CATALOG.map((app) => {
              const active = selectedApp.id === app.id;
              return (
                <TouchableOpacity
                  key={app.id}
                  style={[styles.appChip, active && styles.appChipActive]}
                  onPress={() => setSelectedApp(app)}
                >
                  <Text style={[styles.appChipText, active && styles.appChipTextActive]}>
                    {app.name}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>2) Enter budget (USD)</Text>
          <TextInput
            keyboardType="numeric"
            value={budget}
            onChangeText={setBudget}
            placeholder="e.g. 20"
            placeholderTextColor="#8e9cb5"
            style={styles.input}
          />
          <Text style={styles.result}>
            Estimated purchased credits for {selectedApp.name}:{' '}
            <Text style={styles.resultStrong}>{projectedCredits}</Text>
          </Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>3) Legit reward strategy</Text>
          {LEGIT_EARNING_OPTIONS.map((option) => (
            <View style={styles.rewardRow} key={option.label}>
              <Text style={styles.rewardLabel}>{option.label}</Text>
              <Text style={styles.rewardValue}>+{option.creditsPerDay}/day</Text>
            </View>
          ))}
          <Text style={styles.footerResult}>
            Potential free credits/month: {projectedMonthlyFreeCredits}
          </Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#0b1320',
  },
  container: {
    padding: 20,
    gap: 16,
  },
  heading: {
    fontSize: 28,
    color: '#e9edf7',
    fontWeight: '700',
  },
  subheading: {
    color: '#b8c1d9',
    lineHeight: 22,
  },
  card: {
    backgroundColor: '#172338',
    borderRadius: 14,
    padding: 16,
    borderWidth: 1,
    borderColor: '#253550',
    gap: 12,
  },
  cardTitle: {
    color: '#dbe5f8',
    fontWeight: '600',
    fontSize: 16,
  },
  appList: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  appChip: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 999,
    backgroundColor: '#24344d',
  },
  appChipActive: {
    backgroundColor: '#53a2ff',
  },
  appChipText: {
    color: '#d4def5',
    fontWeight: '500',
  },
  appChipTextActive: {
    color: '#071429',
    fontWeight: '700',
  },
  input: {
    backgroundColor: '#0f1a2d',
    borderWidth: 1,
    borderColor: '#2d3f60',
    borderRadius: 10,
    color: '#f0f5ff',
    padding: 12,
    fontSize: 16,
  },
  result: {
    color: '#cae0ff',
    lineHeight: 20,
  },
  resultStrong: {
    color: '#84d292',
    fontWeight: '700',
  },
  rewardRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  rewardLabel: {
    color: '#d8e1f6',
  },
  rewardValue: {
    color: '#80d492',
    fontWeight: '600',
  },
  footerResult: {
    marginTop: 6,
    color: '#e8f3ff',
    fontWeight: '700',
  },
});
