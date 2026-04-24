import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, ActivityIndicator } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

export default function AccountTabSecurity({
  Colors,
  styles,
  newPwd,
  setNewPwd,
  confirmPwd,
  setConfirmPwd,
  showNewPwd,
  setShowNewPwd,
  savingPwd,
  handleChangePassword,
  userEmail,
  newEmail,
  setNewEmail,
  savingEmail,
  handleChangeEmail,
}: any) {
  return (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {/* Change Password */}
      <Text style={styles.sectionHeader}>CHANGE PASSWORD</Text>

      <Text style={styles.label}>NEW PASSWORD</Text>
      <View style={styles.pwdRow}>
        <TextInput style={[styles.input, { flex: 1, marginBottom: 0 }]}
          value={newPwd} onChangeText={setNewPwd}
          placeholder="Min. 8 characters" placeholderTextColor={Colors.textMuted}
          secureTextEntry={!showNewPwd} autoCapitalize="none" />
        <TouchableOpacity style={styles.eyeBtn} onPress={() => setShowNewPwd(!showNewPwd)}>
          <MaterialCommunityIcons name={showNewPwd ? 'eye-off' : 'eye'} size={18} color={Colors.textMuted} />
        </TouchableOpacity>
      </View>

      <Text style={styles.label}>CONFIRM NEW PASSWORD</Text>
      <TextInput style={styles.input}
        value={confirmPwd} onChangeText={setConfirmPwd}
        placeholder="Repeat new password" placeholderTextColor={Colors.textMuted}
        secureTextEntry={!showNewPwd} autoCapitalize="none" />

      <TouchableOpacity style={[styles.primaryBtn, savingPwd && { opacity: 0.5 }]}
        onPress={handleChangePassword} disabled={savingPwd}>
        {savingPwd ? <ActivityIndicator color="#000" /> : (
          <>
            <MaterialCommunityIcons name="lock-reset" size={16} color="#000" />
            <Text style={styles.primaryBtnText}>Update Password</Text>
          </>
        )}
      </TouchableOpacity>

      {/* Change Email */}
      <Text style={[styles.sectionHeader, { marginTop: Spacing.xxl }]}>CHANGE EMAIL</Text>
      <Text style={styles.currentEmail}>Current: {userEmail}</Text>

      <Text style={styles.label}>NEW EMAIL ADDRESS</Text>
      <TextInput style={styles.input}
        value={newEmail} onChangeText={setNewEmail}
        placeholder="new@email.com" placeholderTextColor={Colors.textMuted}
        keyboardType="email-address" autoCapitalize="none" />

      <TouchableOpacity style={[styles.secondaryBtn, savingEmail && { opacity: 0.5 }]}
        onPress={handleChangeEmail} disabled={savingEmail}>
        {savingEmail ? <ActivityIndicator color={Colors.primary} /> : (
          <>
            <MaterialCommunityIcons name="email-sync" size={16} color={Colors.primary} />
            <Text style={styles.secondaryBtnText}>Send Confirmation Email</Text>
          </>
        )}
      </TouchableOpacity>

      <View style={{ height: 20 }} />
    </ScrollView>
  );
}
