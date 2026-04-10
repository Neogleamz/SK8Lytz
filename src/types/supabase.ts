export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export type Database = {
  // Allows to automatically instantiate createClient with right options
  // instead of createClient<Database, { PostgrestVersion: 'XX' }>(URL, KEY)
  __InternalSupabase: {
    PostgrestVersion: "14.4"
  }
  public: {
    Tables: {
      app_settings: {
        Row: {
          created_at: string
          setting_key: string
          setting_value: Json | null
        }
        Insert: {
          created_at?: string
          setting_key: string
          setting_value?: Json | null
        }
        Update: {
          created_at?: string
          setting_key?: string
          setting_value?: Json | null
        }
        Relationships: []
      }
      crew_members: {
        Row: {
          display_name: string | null
          id: string
          joined_at: string
          session_id: string
          user_id: string | null
        }
        Insert: {
          display_name?: string | null
          id?: string
          joined_at?: string
          session_id: string
          user_id?: string | null
        }
        Update: {
          display_name?: string | null
          id?: string
          joined_at?: string
          session_id?: string
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "crew_members_session_id_fkey"
            columns: ["session_id"]
            isOneToOne: false
            referencedRelation: "crew_sessions"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "crew_members_session_id_fkey"
            columns: ["session_id"]
            isOneToOne: false
            referencedRelation: "public_sessions"
            referencedColumns: ["id"]
          },
        ]
      }
      crew_memberships: {
        Row: {
          crew_id: string
          id: string
          joined_at: string
          role: string | null
          user_id: string
        }
        Insert: {
          crew_id: string
          id?: string
          joined_at?: string
          role?: string | null
          user_id: string
        }
        Update: {
          crew_id?: string
          id?: string
          joined_at?: string
          role?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "crew_memberships_crew_id_fkey"
            columns: ["crew_id"]
            isOneToOne: false
            referencedRelation: "crews"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "crew_memberships_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "user_profiles"
            referencedColumns: ["user_id"]
          },
        ]
      }
      crew_sessions: {
        Row: {
          created_at: string
          crew_id: string | null
          ended_at: string | null
          expires_at: string
          id: string
          invite_code: string
          is_active: boolean
          is_public: boolean
          last_scene: Json | null
          leader_user_id: string | null
          location_coords: Json | null
          location_label: string | null
          name: string
          scheduled_at: string | null
          status: string
        }
        Insert: {
          created_at?: string
          crew_id?: string | null
          ended_at?: string | null
          expires_at?: string
          id?: string
          invite_code?: string
          is_active?: boolean
          is_public?: boolean
          last_scene?: Json | null
          leader_user_id?: string | null
          location_coords?: Json | null
          location_label?: string | null
          name?: string
          scheduled_at?: string | null
          status?: string
        }
        Update: {
          created_at?: string
          crew_id?: string | null
          ended_at?: string | null
          expires_at?: string
          id?: string
          invite_code?: string
          is_active?: boolean
          is_public?: boolean
          last_scene?: Json | null
          leader_user_id?: string | null
          location_coords?: Json | null
          location_label?: string | null
          name?: string
          scheduled_at?: string | null
          status?: string
        }
        Relationships: [
          {
            foreignKeyName: "crew_sessions_crew_id_fkey"
            columns: ["crew_id"]
            isOneToOne: false
            referencedRelation: "crews"
            referencedColumns: ["id"]
          },
        ]
      }
      crews: {
        Row: {
          avatar_color: string | null
          avatar_icon: string | null
          avatar_url: string | null
          city: string | null
          created_at: string
          description: string | null
          id: string
          invite_code: string
          is_public: boolean
          name: string
          owner_id: string | null
          state: string | null
        }
        Insert: {
          avatar_color?: string | null
          avatar_icon?: string | null
          avatar_url?: string | null
          city?: string | null
          created_at?: string
          description?: string | null
          id?: string
          invite_code?: string
          is_public?: boolean
          name: string
          owner_id?: string | null
          state?: string | null
        }
        Update: {
          avatar_color?: string | null
          avatar_icon?: string | null
          avatar_url?: string | null
          city?: string | null
          created_at?: string
          description?: string | null
          id?: string
          invite_code?: string
          is_public?: boolean
          name?: string
          owner_id?: string | null
          state?: string | null
        }
        Relationships: []
      }
      custom_builder_presets: {
        Row: {
          created_at: string | null
          fill_mode: string
          id: string
          name: string
          nodes: Json
          transition_type: number
          updated_at: string | null
          user_id: string | null
        }
        Insert: {
          created_at?: string | null
          fill_mode: string
          id?: string
          name: string
          nodes: Json
          transition_type: number
          updated_at?: string | null
          user_id?: string | null
        }
        Update: {
          created_at?: string | null
          fill_mode?: string
          id?: string
          name?: string
          nodes?: Json
          transition_type?: number
          updated_at?: string | null
          user_id?: string | null
        }
        Relationships: []
      }
      device_diagnostics: {
        Row: {
          byte_0: number | null
          byte_2: number | null
          color_order: string | null
          color_sorting: number | null
          created_at: string
          device_id: string
          device_name: string | null
          ic_name: string | null
          ic_type: number | null
          id: number
          notes: string | null
          parsed_ok: boolean | null
          payload_bytes: number
          payload_hex: string
          points: number | null
          session_id: string | null
        }
        Insert: {
          byte_0?: number | null
          byte_2?: number | null
          color_order?: string | null
          color_sorting?: number | null
          created_at?: string
          device_id: string
          device_name?: string | null
          ic_name?: string | null
          ic_type?: number | null
          id?: number
          notes?: string | null
          parsed_ok?: boolean | null
          payload_bytes: number
          payload_hex: string
          points?: number | null
          session_id?: string | null
        }
        Update: {
          byte_0?: number | null
          byte_2?: number | null
          color_order?: string | null
          color_sorting?: number | null
          created_at?: string
          device_id?: string
          device_name?: string | null
          ic_name?: string | null
          ic_type?: number | null
          id?: number
          notes?: string | null
          parsed_ok?: boolean | null
          payload_bytes?: number
          payload_hex?: string
          points?: number | null
          session_id?: string | null
        }
        Relationships: []
      }
      full_landed_costs: {
        Row: {
          actual_chargeable_weight_g: number | null
          actual_paid: number | null
          actual_shipping_fee: number | null
          alibaba_order: string | null
          corner_protector: number | null
          coupon_discount: number | null
          created_at: string | null
          custom_clearance_fee: number | null
          di_item_id: string | null
          discount_code: number | null
          epe_loose_filling: number | null
          final_cost_weight: number | null
          first_tier_cost: number | null
          fuel_surcharge: number | null
          id: number
          insurance: number | null
          is_3d_print: boolean | null
          is_filament: boolean | null
          item_name: string | null
          lot_multiplier: number | null
          moister_barrier_bag: number | null
          neogleamz_name: string | null
          neogleamz_product: string | null
          one_percent_discount: number | null
          operating_cost: number | null
          order_date: string | null
          order_no: string | null
          order_postage: number | null
          order_total: number | null
          order_unit_price: number | null
          packing_video: number | null
          parcel_no: string | null
          points_discount: number | null
          print_grams: number | null
          print_time_mins: number | null
          quantity: number | null
          remote_area_surcharge: number | null
          second_tier_cost: number | null
          specification: string | null
          storage_fee: number | null
          tax: number | null
          total_cost_weight: number | null
          total_dist_weight_g: number | null
          unit_china_landed_price: number | null
          unit_ship_weight: number | null
          unit_weight_g: number | null
        }
        Insert: {
          actual_chargeable_weight_g?: number | null
          actual_paid?: number | null
          actual_shipping_fee?: number | null
          alibaba_order?: string | null
          corner_protector?: number | null
          coupon_discount?: number | null
          created_at?: string | null
          custom_clearance_fee?: number | null
          di_item_id?: string | null
          discount_code?: number | null
          epe_loose_filling?: number | null
          final_cost_weight?: number | null
          first_tier_cost?: number | null
          fuel_surcharge?: number | null
          id?: number
          insurance?: number | null
          is_3d_print?: boolean | null
          is_filament?: boolean | null
          item_name?: string | null
          lot_multiplier?: number | null
          moister_barrier_bag?: number | null
          neogleamz_name?: string | null
          neogleamz_product?: string | null
          one_percent_discount?: number | null
          operating_cost?: number | null
          order_date?: string | null
          order_no?: string | null
          order_postage?: number | null
          order_total?: number | null
          order_unit_price?: number | null
          packing_video?: number | null
          parcel_no?: string | null
          points_discount?: number | null
          print_grams?: number | null
          print_time_mins?: number | null
          quantity?: number | null
          remote_area_surcharge?: number | null
          second_tier_cost?: number | null
          specification?: string | null
          storage_fee?: number | null
          tax?: number | null
          total_cost_weight?: number | null
          total_dist_weight_g?: number | null
          unit_china_landed_price?: number | null
          unit_ship_weight?: number | null
          unit_weight_g?: number | null
        }
        Update: {
          actual_chargeable_weight_g?: number | null
          actual_paid?: number | null
          actual_shipping_fee?: number | null
          alibaba_order?: string | null
          corner_protector?: number | null
          coupon_discount?: number | null
          created_at?: string | null
          custom_clearance_fee?: number | null
          di_item_id?: string | null
          discount_code?: number | null
          epe_loose_filling?: number | null
          final_cost_weight?: number | null
          first_tier_cost?: number | null
          fuel_surcharge?: number | null
          id?: number
          insurance?: number | null
          is_3d_print?: boolean | null
          is_filament?: boolean | null
          item_name?: string | null
          lot_multiplier?: number | null
          moister_barrier_bag?: number | null
          neogleamz_name?: string | null
          neogleamz_product?: string | null
          one_percent_discount?: number | null
          operating_cost?: number | null
          order_date?: string | null
          order_no?: string | null
          order_postage?: number | null
          order_total?: number | null
          order_unit_price?: number | null
          packing_video?: number | null
          parcel_no?: string | null
          points_discount?: number | null
          print_grams?: number | null
          print_time_mins?: number | null
          quantity?: number | null
          remote_area_surcharge?: number | null
          second_tier_cost?: number | null
          specification?: string | null
          storage_fee?: number | null
          tax?: number | null
          total_cost_weight?: number | null
          total_dist_weight_g?: number | null
          unit_china_landed_price?: number | null
          unit_ship_weight?: number | null
          unit_weight_g?: number | null
        }
        Relationships: []
      }
      inventory_consumption: {
        Row: {
          assembly_consumed_qty: number | null
          consumed_qty: number | null
          item_key: string
          manual_adjustment: number | null
          min_stock: number | null
          produced_qty: number | null
          production_consumed_qty: number | null
          prototype_consumed_qty: number | null
          prototype_produced_qty: number | null
          scrap_qty: number | null
          sold_qty: number | null
        }
        Insert: {
          assembly_consumed_qty?: number | null
          consumed_qty?: number | null
          item_key: string
          manual_adjustment?: number | null
          min_stock?: number | null
          produced_qty?: number | null
          production_consumed_qty?: number | null
          prototype_consumed_qty?: number | null
          prototype_produced_qty?: number | null
          scrap_qty?: number | null
          sold_qty?: number | null
        }
        Update: {
          assembly_consumed_qty?: number | null
          consumed_qty?: number | null
          item_key?: string
          manual_adjustment?: number | null
          min_stock?: number | null
          produced_qty?: number | null
          production_consumed_qty?: number | null
          prototype_consumed_qty?: number | null
          prototype_produced_qty?: number | null
          scrap_qty?: number | null
          sold_qty?: number | null
        }
        Relationships: []
      }
      label_designs: {
        Row: {
          created_at: string | null
          emoji: string | null
          file_name: string | null
          file_url: string | null
          id: string
          label_size: string | null
          layout_json: Json | null
          product_name: string
          updated_at: string | null
        }
        Insert: {
          created_at?: string | null
          emoji?: string | null
          file_name?: string | null
          file_url?: string | null
          id?: string
          label_size?: string | null
          layout_json?: Json | null
          product_name: string
          updated_at?: string | null
        }
        Update: {
          created_at?: string | null
          emoji?: string | null
          file_name?: string | null
          file_url?: string | null
          id?: string
          label_size?: string | null
          layout_json?: Json | null
          product_name?: string
          updated_at?: string | null
        }
        Relationships: []
      }
      led_diagnostics: {
        Row: {
          color_b: number | null
          color_g: number | null
          color_order_sent: string | null
          color_r: number | null
          created_at: string | null
          device_id: string | null
          device_name: string | null
          direction_val: number | null
          expected_behavior: string | null
          hw_color_sort: string | null
          hw_detected: boolean | null
          hw_ic_type: string | null
          hw_led_count: number | null
          id: string
          is_correct: boolean | null
          num_points: number | null
          payload_hex: string
          payload_params: Json | null
          physical_result: string | null
          protocol: string
          session_notes: string | null
          speed_val: number | null
          test_label: string | null
          transition_type: number | null
          user_id: string | null
        }
        Insert: {
          color_b?: number | null
          color_g?: number | null
          color_order_sent?: string | null
          color_r?: number | null
          created_at?: string | null
          device_id?: string | null
          device_name?: string | null
          direction_val?: number | null
          expected_behavior?: string | null
          hw_color_sort?: string | null
          hw_detected?: boolean | null
          hw_ic_type?: string | null
          hw_led_count?: number | null
          id?: string
          is_correct?: boolean | null
          num_points?: number | null
          payload_hex: string
          payload_params?: Json | null
          physical_result?: string | null
          protocol: string
          session_notes?: string | null
          speed_val?: number | null
          test_label?: string | null
          transition_type?: number | null
          user_id?: string | null
        }
        Update: {
          color_b?: number | null
          color_g?: number | null
          color_order_sent?: string | null
          color_r?: number | null
          created_at?: string | null
          device_id?: string | null
          device_name?: string | null
          direction_val?: number | null
          expected_behavior?: string | null
          hw_color_sort?: string | null
          hw_detected?: boolean | null
          hw_ic_type?: string | null
          hw_led_count?: number | null
          id?: string
          is_correct?: boolean | null
          num_points?: number | null
          payload_hex?: string
          payload_params?: Json | null
          physical_result?: string | null
          protocol?: string
          session_notes?: string | null
          speed_val?: number | null
          test_label?: string | null
          transition_type?: number | null
          user_id?: string | null
        }
        Relationships: []
      }
      pack_ship_sops: {
        Row: {
          created_at: string | null
          id: string
          instruction_json: Json
          internal_recipe_name: string
          required_box_sku: string | null
          updated_at: string | null
        }
        Insert: {
          created_at?: string | null
          id?: string
          instruction_json?: Json
          internal_recipe_name: string
          required_box_sku?: string | null
          updated_at?: string | null
        }
        Update: {
          created_at?: string | null
          id?: string
          instruction_json?: Json
          internal_recipe_name?: string
          required_box_sku?: string | null
          updated_at?: string | null
        }
        Relationships: []
      }
      parsed_color_usage: {
        Row: {
          created_at: string
          device_id: string | null
          device_name: string | null
          group_id: string | null
          group_name: string | null
          hex_color: string | null
          id: string
          session_id: string | null
          timestamp_ms: number | null
          usage_count: number | null
        }
        Insert: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          hex_color?: string | null
          id?: string
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Update: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          hex_color?: string | null
          id?: string
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Relationships: []
      }
      parsed_logs: {
        Row: {
          created_at: string
          device_id: string | null
          device_name: string | null
          direction: string | null
          event_type: string | null
          group_id: string | null
          group_name: string | null
          hex_payload: string | null
          host_device_id: string | null
          id: string
          raw_data: Json | null
          session_id: string | null
          timestamp_ms: number | null
        }
        Insert: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          direction?: string | null
          event_type?: string | null
          group_id?: string | null
          group_name?: string | null
          hex_payload?: string | null
          host_device_id?: string | null
          id?: string
          raw_data?: Json | null
          session_id?: string | null
          timestamp_ms?: number | null
        }
        Update: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          direction?: string | null
          event_type?: string | null
          group_id?: string | null
          group_name?: string | null
          hex_payload?: string | null
          host_device_id?: string | null
          id?: string
          raw_data?: Json | null
          session_id?: string | null
          timestamp_ms?: number | null
        }
        Relationships: []
      }
      parsed_mode_usage: {
        Row: {
          created_at: string
          device_id: string | null
          device_name: string | null
          group_id: string | null
          group_name: string | null
          id: string
          mode_name: string | null
          session_id: string | null
          timestamp_ms: number | null
          usage_count: number | null
        }
        Insert: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          id?: string
          mode_name?: string | null
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Update: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          id?: string
          mode_name?: string | null
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Relationships: []
      }
      parsed_pattern_usage: {
        Row: {
          created_at: string
          device_id: string | null
          device_name: string | null
          group_id: string | null
          group_name: string | null
          id: string
          pattern_idx: string | null
          session_id: string | null
          timestamp_ms: number | null
          usage_count: number | null
        }
        Insert: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          id?: string
          pattern_idx?: string | null
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Update: {
          created_at?: string
          device_id?: string | null
          device_name?: string | null
          group_id?: string | null
          group_name?: string | null
          id?: string
          pattern_idx?: string | null
          session_id?: string | null
          timestamp_ms?: number | null
          usage_count?: number | null
        }
        Relationships: []
      }
      parsed_session_devices: {
        Row: {
          color_sorting: string | null
          created_at: string
          device_id: string | null
          firmware_ver: Json | null
          group_id: string | null
          group_name: string | null
          host_device_id: string | null
          ic_type: string | null
          id: string
          led_points: number | null
          name: string | null
          rssi: number | null
          segments: number | null
          session_id: string | null
          timestamp_ms: number | null
        }
        Insert: {
          color_sorting?: string | null
          created_at?: string
          device_id?: string | null
          firmware_ver?: Json | null
          group_id?: string | null
          group_name?: string | null
          host_device_id?: string | null
          ic_type?: string | null
          id?: string
          led_points?: number | null
          name?: string | null
          rssi?: number | null
          segments?: number | null
          session_id?: string | null
          timestamp_ms?: number | null
        }
        Update: {
          color_sorting?: string | null
          created_at?: string
          device_id?: string | null
          firmware_ver?: Json | null
          group_id?: string | null
          group_name?: string | null
          host_device_id?: string | null
          ic_type?: string | null
          id?: string
          led_points?: number | null
          name?: string | null
          rssi?: number | null
          segments?: number | null
          session_id?: string | null
          timestamp_ms?: number | null
        }
        Relationships: []
      }
      parsed_session_stats: {
        Row: {
          average_load_time_ms: number | null
          battery_level: number | null
          battery_state: string | null
          color_usage: Json | null
          created_at: string
          device_brand: string | null
          device_id: string | null
          device_manufacturer: string | null
          device_model: string | null
          device_model_id: string | null
          device_name: string | null
          device_type: string | null
          device_year_class: number | null
          devices_discovered: number | null
          group_id: string | null
          group_name: string | null
          host_device_id: string | null
          host_device_info: Json | null
          id: string
          is_low_power_mode: boolean | null
          is_physical_device: boolean | null
          last_app_opened_time: number | null
          mode_usage: Json | null
          os_build_id: string | null
          os_name: string | null
          os_version: string | null
          platform_api_level: number | null
          primary_ble_mac: string | null
          session_id: string | null
          storage_bytes_estimate: number | null
          timestamp_ms: number | null
          total_events: number | null
          total_memory_mb: number | null
          total_storage_estimate: number | null
        }
        Insert: {
          average_load_time_ms?: number | null
          battery_level?: number | null
          battery_state?: string | null
          color_usage?: Json | null
          created_at?: string
          device_brand?: string | null
          device_id?: string | null
          device_manufacturer?: string | null
          device_model?: string | null
          device_model_id?: string | null
          device_name?: string | null
          device_type?: string | null
          device_year_class?: number | null
          devices_discovered?: number | null
          group_id?: string | null
          group_name?: string | null
          host_device_id?: string | null
          host_device_info?: Json | null
          id?: string
          is_low_power_mode?: boolean | null
          is_physical_device?: boolean | null
          last_app_opened_time?: number | null
          mode_usage?: Json | null
          os_build_id?: string | null
          os_name?: string | null
          os_version?: string | null
          platform_api_level?: number | null
          primary_ble_mac?: string | null
          session_id?: string | null
          storage_bytes_estimate?: number | null
          timestamp_ms?: number | null
          total_events?: number | null
          total_memory_mb?: number | null
          total_storage_estimate?: number | null
        }
        Update: {
          average_load_time_ms?: number | null
          battery_level?: number | null
          battery_state?: string | null
          color_usage?: Json | null
          created_at?: string
          device_brand?: string | null
          device_id?: string | null
          device_manufacturer?: string | null
          device_model?: string | null
          device_model_id?: string | null
          device_name?: string | null
          device_type?: string | null
          device_year_class?: number | null
          devices_discovered?: number | null
          group_id?: string | null
          group_name?: string | null
          host_device_id?: string | null
          host_device_info?: Json | null
          id?: string
          is_low_power_mode?: boolean | null
          is_physical_device?: boolean | null
          last_app_opened_time?: number | null
          mode_usage?: Json | null
          os_build_id?: string | null
          os_name?: string | null
          os_version?: string | null
          platform_api_level?: number | null
          primary_ble_mac?: string | null
          session_id?: string | null
          storage_bytes_estimate?: number | null
          timestamp_ms?: number | null
          total_events?: number | null
          total_memory_mb?: number | null
          total_storage_estimate?: number | null
        }
        Relationships: []
      }
      print_queue: {
        Row: {
          completed_at: string | null
          created_at: string | null
          id: string
          label: string | null
          part_name: string
          qty: number
          started_at: string | null
          status: string
          wo_id: string | null
        }
        Insert: {
          completed_at?: string | null
          created_at?: string | null
          id?: string
          label?: string | null
          part_name: string
          qty?: number
          started_at?: string | null
          status?: string
          wo_id?: string | null
        }
        Update: {
          completed_at?: string | null
          created_at?: string | null
          id?: string
          label?: string | null
          part_name?: string
          qty?: number
          started_at?: string | null
          status?: string
          wo_id?: string | null
        }
        Relationships: []
      }
      product_recipes: {
        Row: {
          affiliate_pct: number | null
          components: Json | null
          created_at: string | null
          filament_item_key: string | null
          is_3d_print: boolean | null
          is_label: boolean | null
          is_subassembly: boolean | null
          label_emoji: string | null
          labor_rate_hr: number | null
          labor_time_mins: number | null
          msrp: number | null
          old_msrp: number | null
          print_grams: number | null
          print_time_mins: number | null
          product_name: string
          warranty_pct: number | null
          wholesale_price: number | null
        }
        Insert: {
          affiliate_pct?: number | null
          components?: Json | null
          created_at?: string | null
          filament_item_key?: string | null
          is_3d_print?: boolean | null
          is_label?: boolean | null
          is_subassembly?: boolean | null
          label_emoji?: string | null
          labor_rate_hr?: number | null
          labor_time_mins?: number | null
          msrp?: number | null
          old_msrp?: number | null
          print_grams?: number | null
          print_time_mins?: number | null
          product_name: string
          warranty_pct?: number | null
          wholesale_price?: number | null
        }
        Update: {
          affiliate_pct?: number | null
          components?: Json | null
          created_at?: string | null
          filament_item_key?: string | null
          is_3d_print?: boolean | null
          is_label?: boolean | null
          is_subassembly?: boolean | null
          label_emoji?: string | null
          labor_rate_hr?: number | null
          labor_time_mins?: number | null
          msrp?: number | null
          old_msrp?: number | null
          print_grams?: number | null
          print_time_mins?: number | null
          product_name?: string
          warranty_pct?: number | null
          wholesale_price?: number | null
        }
        Relationships: []
      }
      production_sops: {
        Row: {
          created_at: string
          product_name: string
          steps: Json | null
        }
        Insert: {
          created_at?: string
          product_name: string
          steps?: Json | null
        }
        Update: {
          created_at?: string
          product_name?: string
          steps?: Json | null
        }
        Relationships: []
      }
      push_tokens: {
        Row: {
          id: string
          platform: string
          token: string
          updated_at: string
          user_id: string
        }
        Insert: {
          id?: string
          platform: string
          token: string
          updated_at?: string
          user_id: string
        }
        Update: {
          id?: string
          platform?: string
          token?: string
          updated_at?: string
          user_id?: string
        }
        Relationships: []
      }
      raw_orders: {
        Row: {
          alibaba_order: string | null
          di_item_id: string
          item_name: string | null
          order_date: string | null
          order_no: string | null
          order_total: number | null
          postage: number | null
          quantity: number | null
          specification: string | null
          unit_china_landed_price: number | null
          unit_price: number | null
        }
        Insert: {
          alibaba_order?: string | null
          di_item_id: string
          item_name?: string | null
          order_date?: string | null
          order_no?: string | null
          order_total?: number | null
          postage?: number | null
          quantity?: number | null
          specification?: string | null
          unit_china_landed_price?: number | null
          unit_price?: number | null
        }
        Update: {
          alibaba_order?: string | null
          di_item_id?: string
          item_name?: string | null
          order_date?: string | null
          order_no?: string | null
          order_total?: number | null
          postage?: number | null
          quantity?: number | null
          specification?: string | null
          unit_china_landed_price?: number | null
          unit_price?: number | null
        }
        Relationships: []
      }
      raw_parcel_items: {
        Row: {
          di_item_id: string | null
          id: number
          item_name: string | null
          parcel_no: string | null
          quantity: number | null
          specification: string | null
          total_dist_weight_g: number | null
          unit_weight_g: number | null
        }
        Insert: {
          di_item_id?: string | null
          id?: number
          item_name?: string | null
          parcel_no?: string | null
          quantity?: number | null
          specification?: string | null
          total_dist_weight_g?: number | null
          unit_weight_g?: number | null
        }
        Update: {
          di_item_id?: string | null
          id?: number
          item_name?: string | null
          parcel_no?: string | null
          quantity?: number | null
          specification?: string | null
          total_dist_weight_g?: number | null
          unit_weight_g?: number | null
        }
        Relationships: []
      }
      raw_parcel_summary: {
        Row: {
          actual_chargeable_weight_g: number | null
          actual_paid: number | null
          actual_shipping_fee: number | null
          corner_protector: number | null
          coupon_discount: number | null
          custom_clearance_fee: number | null
          discount_code: number | null
          epe_loose_filling: number | null
          first_tier_cost: number | null
          fuel_surcharge: number | null
          insurance: number | null
          moister_barrier_bag: number | null
          one_percent_discount: number | null
          operating_cost: number | null
          packing_video: number | null
          parcel_no: string
          points_discount: number | null
          remote_area_surcharge: number | null
          second_tier_cost: number | null
          storage_fee: number | null
          tax: number | null
        }
        Insert: {
          actual_chargeable_weight_g?: number | null
          actual_paid?: number | null
          actual_shipping_fee?: number | null
          corner_protector?: number | null
          coupon_discount?: number | null
          custom_clearance_fee?: number | null
          discount_code?: number | null
          epe_loose_filling?: number | null
          first_tier_cost?: number | null
          fuel_surcharge?: number | null
          insurance?: number | null
          moister_barrier_bag?: number | null
          one_percent_discount?: number | null
          operating_cost?: number | null
          packing_video?: number | null
          parcel_no: string
          points_discount?: number | null
          remote_area_surcharge?: number | null
          second_tier_cost?: number | null
          storage_fee?: number | null
          tax?: number | null
        }
        Update: {
          actual_chargeable_weight_g?: number | null
          actual_paid?: number | null
          actual_shipping_fee?: number | null
          corner_protector?: number | null
          coupon_discount?: number | null
          custom_clearance_fee?: number | null
          discount_code?: number | null
          epe_loose_filling?: number | null
          first_tier_cost?: number | null
          fuel_surcharge?: number | null
          insurance?: number | null
          moister_barrier_bag?: number | null
          one_percent_discount?: number | null
          operating_cost?: number | null
          packing_video?: number | null
          parcel_no?: string
          points_discount?: number | null
          remote_area_surcharge?: number | null
          second_tier_cost?: number | null
          storage_fee?: number | null
          tax?: number | null
        }
        Relationships: []
      }
      registered_devices: {
        Row: {
          color_sorting: string | null
          created_at: string
          custom_name: string
          device_mac: string | null
          device_name: string | null
          firmware_ver: number | null
          group_id: string
          group_name: string | null
          ic_type: string | null
          id: string
          is_pending_sync: boolean | null
          led_points: number | null
          led_version: number | null
          points: number
          position: string | null
          product_id: number | null
          product_type: string | null
          registered_at: string | null
          rssi_at_register: number | null
          segments: number
          sorting: string
          strip_type: string
          updated_at: string | null
          user_id: string
        }
        Insert: {
          color_sorting?: string | null
          created_at?: string
          custom_name: string
          device_mac?: string | null
          device_name?: string | null
          firmware_ver?: number | null
          group_id: string
          group_name?: string | null
          ic_type?: string | null
          id: string
          is_pending_sync?: boolean | null
          led_points?: number | null
          led_version?: number | null
          points: number
          position?: string | null
          product_id?: number | null
          product_type?: string | null
          registered_at?: string | null
          rssi_at_register?: number | null
          segments: number
          sorting: string
          strip_type: string
          updated_at?: string | null
          user_id: string
        }
        Update: {
          color_sorting?: string | null
          created_at?: string
          custom_name?: string
          device_mac?: string | null
          device_name?: string | null
          firmware_ver?: number | null
          group_id?: string
          group_name?: string | null
          ic_type?: string | null
          id?: string
          is_pending_sync?: boolean | null
          led_points?: number | null
          led_version?: number | null
          points?: number
          position?: string | null
          product_id?: number | null
          product_type?: string | null
          registered_at?: string | null
          rssi_at_register?: number | null
          segments?: number
          sorting?: string
          strip_type?: string
          updated_at?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "registered_devices_group_id_fkey"
            columns: ["group_id"]
            isOneToOne: false
            referencedRelation: "registered_groups"
            referencedColumns: ["id"]
          },
        ]
      }
      registered_groups: {
        Row: {
          created_at: string
          group_name: string
          id: string
          type: string
          user_id: string
        }
        Insert: {
          created_at?: string
          group_name: string
          id: string
          type: string
          user_id: string
        }
        Update: {
          created_at?: string
          group_name?: string
          id?: string
          type?: string
          user_id?: string
        }
        Relationships: []
      }
      remote_debug_logs: {
        Row: {
          created_at: string | null
          data: Json | null
          device_info: Json | null
          event_type: string | null
          id: string
          message: string | null
          session_id: string | null
        }
        Insert: {
          created_at?: string | null
          data?: Json | null
          device_info?: Json | null
          event_type?: string | null
          id?: string
          message?: string | null
          session_id?: string | null
        }
        Update: {
          created_at?: string | null
          data?: Json | null
          device_info?: Json | null
          event_type?: string | null
          id?: string
          message?: string | null
          session_id?: string | null
        }
        Relationships: []
      }
      sales_ledger: {
        Row: {
          actual_sale_price: number | null
          assembly_completed_at: string | null
          cogs_at_sale: number | null
          created_at: string
          currency: string | null
          customer_email_hash: string | null
          customer_phone_hash: string | null
          discount: number | null
          discount_amount: number | null
          discount_code: string | null
          exchange_value: number | null
          financial_status: string | null
          fulfillment_status: string | null
          id: string
          internal_fulfillment_status: string | null
          internal_recipe_name: string | null
          isFirstRow: boolean | null
          lineitem_compare_at_price: number | null
          lineitem_fulfillment_status: string | null
          linked_order_id: string | null
          net_profit: number | null
          order_id: string
          "Outstanding Balance": number | null
          payment_method: string | null
          qa_cleared_at: string | null
          qty_sold: number | null
          refunded_amount: number | null
          risk_level: string | null
          sale_date: string | null
          shipping: number | null
          shipping_address_hash: string | null
          shipping_city: string | null
          shipping_country: string | null
          shipping_method: string | null
          shipping_name_hash: string | null
          shipping_province: string | null
          shipping_zip: string | null
          Source: string | null
          storefront_sku: string | null
          subtotal: number | null
          tags: string | null
          taxes: number | null
          total: number | null
          transaction_fees: number | null
          transaction_type: string | null
        }
        Insert: {
          actual_sale_price?: number | null
          assembly_completed_at?: string | null
          cogs_at_sale?: number | null
          created_at?: string
          currency?: string | null
          customer_email_hash?: string | null
          customer_phone_hash?: string | null
          discount?: number | null
          discount_amount?: number | null
          discount_code?: string | null
          exchange_value?: number | null
          financial_status?: string | null
          fulfillment_status?: string | null
          id?: string
          internal_fulfillment_status?: string | null
          internal_recipe_name?: string | null
          isFirstRow?: boolean | null
          lineitem_compare_at_price?: number | null
          lineitem_fulfillment_status?: string | null
          linked_order_id?: string | null
          net_profit?: number | null
          order_id: string
          "Outstanding Balance"?: number | null
          payment_method?: string | null
          qa_cleared_at?: string | null
          qty_sold?: number | null
          refunded_amount?: number | null
          risk_level?: string | null
          sale_date?: string | null
          shipping?: number | null
          shipping_address_hash?: string | null
          shipping_city?: string | null
          shipping_country?: string | null
          shipping_method?: string | null
          shipping_name_hash?: string | null
          shipping_province?: string | null
          shipping_zip?: string | null
          Source?: string | null
          storefront_sku?: string | null
          subtotal?: number | null
          tags?: string | null
          taxes?: number | null
          total?: number | null
          transaction_fees?: number | null
          transaction_type?: string | null
        }
        Update: {
          actual_sale_price?: number | null
          assembly_completed_at?: string | null
          cogs_at_sale?: number | null
          created_at?: string
          currency?: string | null
          customer_email_hash?: string | null
          customer_phone_hash?: string | null
          discount?: number | null
          discount_amount?: number | null
          discount_code?: string | null
          exchange_value?: number | null
          financial_status?: string | null
          fulfillment_status?: string | null
          id?: string
          internal_fulfillment_status?: string | null
          internal_recipe_name?: string | null
          isFirstRow?: boolean | null
          lineitem_compare_at_price?: number | null
          lineitem_fulfillment_status?: string | null
          linked_order_id?: string | null
          net_profit?: number | null
          order_id?: string
          "Outstanding Balance"?: number | null
          payment_method?: string | null
          qa_cleared_at?: string | null
          qty_sold?: number | null
          refunded_amount?: number | null
          risk_level?: string | null
          sale_date?: string | null
          shipping?: number | null
          shipping_address_hash?: string | null
          shipping_city?: string | null
          shipping_country?: string | null
          shipping_method?: string | null
          shipping_name_hash?: string | null
          shipping_province?: string | null
          shipping_zip?: string | null
          Source?: string | null
          storefront_sku?: string | null
          subtotal?: number | null
          tags?: string | null
          taxes?: number | null
          total?: number | null
          transaction_fees?: number | null
          transaction_type?: string | null
        }
        Relationships: []
      }
      shared_scenes: {
        Row: {
          author_id: string | null
          author_username: string
          created_at: string | null
          downloads: number | null
          id: string
          is_public: boolean | null
          name: string
          scene_payload: Json
          upvotes: number | null
        }
        Insert: {
          author_id?: string | null
          author_username: string
          created_at?: string | null
          downloads?: number | null
          id?: string
          is_public?: boolean | null
          name: string
          scene_payload: Json
          upvotes?: number | null
        }
        Update: {
          author_id?: string | null
          author_username?: string
          created_at?: string | null
          downloads?: number | null
          id?: string
          is_public?: boolean | null
          name?: string
          scene_payload?: Json
          upvotes?: number | null
        }
        Relationships: []
      }
      sk8lytz_picks: {
        Row: {
          active_from: string | null
          active_until: string | null
          brightness: number
          color: string | null
          created_at: string
          custom_name: string | null
          fixed_bg_color: string | null
          fixed_color_mode: string | null
          fixed_fg_color: string | null
          fixed_hue: number | null
          id: string
          is_active: boolean
          mic_sensitivity: number | null
          mic_source: string | null
          mode: string
          multi_colors: Json | null
          multi_length: number | null
          multi_transition: number | null
          music_matrix_style: number | null
          music_primary_color: string | null
          music_secondary_color: string | null
          name: string
          pattern_id: number | null
          sort_order: number
          speed: number
          updated_at: string
        }
        Insert: {
          active_from?: string | null
          active_until?: string | null
          brightness?: number
          color?: string | null
          created_at?: string
          custom_name?: string | null
          fixed_bg_color?: string | null
          fixed_color_mode?: string | null
          fixed_fg_color?: string | null
          fixed_hue?: number | null
          id?: string
          is_active?: boolean
          mic_sensitivity?: number | null
          mic_source?: string | null
          mode: string
          multi_colors?: Json | null
          multi_length?: number | null
          multi_transition?: number | null
          music_matrix_style?: number | null
          music_primary_color?: string | null
          music_secondary_color?: string | null
          name: string
          pattern_id?: number | null
          sort_order?: number
          speed?: number
          updated_at?: string
        }
        Update: {
          active_from?: string | null
          active_until?: string | null
          brightness?: number
          color?: string | null
          created_at?: string
          custom_name?: string | null
          fixed_bg_color?: string | null
          fixed_color_mode?: string | null
          fixed_fg_color?: string | null
          fixed_hue?: number | null
          id?: string
          is_active?: boolean
          mic_sensitivity?: number | null
          mic_source?: string | null
          mode?: string
          multi_colors?: Json | null
          multi_length?: number | null
          multi_transition?: number | null
          music_matrix_style?: number | null
          music_primary_color?: string | null
          music_secondary_color?: string | null
          name?: string
          pattern_id?: number | null
          sort_order?: number
          speed?: number
          updated_at?: string
        }
        Relationships: []
      }
      socialz_audience: {
        Row: {
          collab_status: string | null
          collab_tier: string | null
          contact_info: string | null
          created_at: string
          followers_fb: number | null
          followers_ig: number | null
          followers_tt: number | null
          followers_yt: number | null
          handle_fb: string | null
          handle_ig: string | null
          handle_tt: string | null
          handle_yt: string | null
          id: string
          is_favorite: boolean | null
          link_fb: string | null
          link_ig: string | null
          link_tt: string | null
          link_yt: string | null
          location: string | null
          name: string
          raw_followers: number | null
          region: string | null
          skater_type: string | null
          style: string | null
          summary: string | null
          updated_at: string
          viral_url: string | null
        }
        Insert: {
          collab_status?: string | null
          collab_tier?: string | null
          contact_info?: string | null
          created_at?: string
          followers_fb?: number | null
          followers_ig?: number | null
          followers_tt?: number | null
          followers_yt?: number | null
          handle_fb?: string | null
          handle_ig?: string | null
          handle_tt?: string | null
          handle_yt?: string | null
          id?: string
          is_favorite?: boolean | null
          link_fb?: string | null
          link_ig?: string | null
          link_tt?: string | null
          link_yt?: string | null
          location?: string | null
          name: string
          raw_followers?: number | null
          region?: string | null
          skater_type?: string | null
          style?: string | null
          summary?: string | null
          updated_at?: string
          viral_url?: string | null
        }
        Update: {
          collab_status?: string | null
          collab_tier?: string | null
          contact_info?: string | null
          created_at?: string
          followers_fb?: number | null
          followers_ig?: number | null
          followers_tt?: number | null
          followers_yt?: number | null
          handle_fb?: string | null
          handle_ig?: string | null
          handle_tt?: string | null
          handle_yt?: string | null
          id?: string
          is_favorite?: boolean | null
          link_fb?: string | null
          link_ig?: string | null
          link_tt?: string | null
          link_yt?: string | null
          location?: string | null
          name?: string
          raw_followers?: number | null
          region?: string | null
          skater_type?: string | null
          style?: string | null
          summary?: string | null
          updated_at?: string
          viral_url?: string | null
        }
        Relationships: []
      }
      sop_archives: {
        Row: {
          id: string
          internal_recipe_name: string
          order_id: string
          packer_telemetry: Json | null
          qa_passed_at: string
          required_box_sku: string | null
          sop_snapshot: Json | null
        }
        Insert: {
          id?: string
          internal_recipe_name: string
          order_id: string
          packer_telemetry?: Json | null
          qa_passed_at?: string
          required_box_sku?: string | null
          sop_snapshot?: Json | null
        }
        Update: {
          id?: string
          internal_recipe_name?: string
          order_id?: string
          packer_telemetry?: Json | null
          qa_passed_at?: string
          required_box_sku?: string | null
          sop_snapshot?: Json | null
        }
        Relationships: []
      }
      storefront_aliases: {
        Row: {
          created_at: string
          internal_recipe_name: string | null
          platform: string | null
          storefront_sku: string
        }
        Insert: {
          created_at?: string
          internal_recipe_name?: string | null
          platform?: string | null
          storefront_sku: string
        }
        Update: {
          created_at?: string
          internal_recipe_name?: string | null
          platform?: string | null
          storefront_sku?: string
        }
        Relationships: []
      }
      telemetry_errors: {
        Row: {
          created_at: string
          error_message: string
          event_type: string
          id: string
          raw_context: Json | null
          session_id: string | null
          stack_trace: string | null
          status: string
        }
        Insert: {
          created_at?: string
          error_message: string
          event_type: string
          id?: string
          raw_context?: Json | null
          session_id?: string | null
          stack_trace?: string | null
          status?: string
        }
        Update: {
          created_at?: string
          error_message?: string
          event_type?: string
          id?: string
          raw_context?: Json | null
          session_id?: string | null
          stack_trace?: string | null
          status?: string
        }
        Relationships: []
      }
      tipz: {
        Row: {
          created_at: string
          id: number
          priority: string | null
          status: string | null
          suggestion: string
          user_email: string | null
        }
        Insert: {
          created_at?: string
          id?: number
          priority?: string | null
          status?: string | null
          suggestion: string
          user_email?: string | null
        }
        Update: {
          created_at?: string
          id?: number
          priority?: string | null
          status?: string | null
          suggestion?: string
          user_email?: string | null
        }
        Relationships: []
      }
      user_profiles: {
        Row: {
          avatar_color: string
          avatar_url: string | null
          created_at: string
          display_name: string | null
          updated_at: string
          user_id: string
          username: string | null
        }
        Insert: {
          avatar_color?: string
          avatar_url?: string | null
          created_at?: string
          display_name?: string | null
          updated_at?: string
          user_id: string
          username?: string | null
        }
        Update: {
          avatar_color?: string
          avatar_url?: string | null
          created_at?: string
          display_name?: string | null
          updated_at?: string
          user_id?: string
          username?: string | null
        }
        Relationships: []
      }
      work_orders: {
        Row: {
          completed_at: string | null
          created_at: string
          label: string | null
          product_name: string | null
          qty: number | null
          routing: Json | null
          started_at: string | null
          status: string | null
          wip_state: Json | null
          wo_id: string
        }
        Insert: {
          completed_at?: string | null
          created_at?: string
          label?: string | null
          product_name?: string | null
          qty?: number | null
          routing?: Json | null
          started_at?: string | null
          status?: string | null
          wip_state?: Json | null
          wo_id: string
        }
        Update: {
          completed_at?: string | null
          created_at?: string
          label?: string | null
          product_name?: string | null
          qty?: number | null
          routing?: Json | null
          started_at?: string | null
          status?: string | null
          wip_state?: Json | null
          wo_id?: string
        }
        Relationships: []
      }
    }
    Views: {
      public_sessions: {
        Row: {
          created_at: string | null
          crew_name: string | null
          id: string | null
          invite_code: string | null
          leader_name: string | null
          leader_username: string | null
          location_coords: Json | null
          location_label: string | null
          member_count: number | null
          name: string | null
          scheduled_at: string | null
          status: string | null
        }
        Relationships: []
      }
    }
    Functions: {
      get_email_by_username: { Args: { p_username: string }; Returns: string }
      get_nearby_push_tokens: {
        Args: { p_max_tokens?: number; p_session_id: string }
        Returns: {
          platform: string
          token: string
        }[]
      }
      increment_scene_download: {
        Args: { scene_id: string }
        Returns: undefined
      }
      increment_scene_upvote: { Args: { scene_id: string }; Returns: undefined }
    }
    Enums: {
      [_ in never]: never
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

type DatabaseWithoutInternals = Omit<Database, "__InternalSupabase">

type DefaultSchema = DatabaseWithoutInternals[Extract<keyof Database, "public">]

export type Tables<
  DefaultSchemaTableNameOrOptions extends
    | keyof (DefaultSchema["Tables"] & DefaultSchema["Views"])
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
        DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
      DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : DefaultSchemaTableNameOrOptions extends keyof (DefaultSchema["Tables"] &
        DefaultSchema["Views"])
    ? (DefaultSchema["Tables"] &
        DefaultSchema["Views"])[DefaultSchemaTableNameOrOptions] extends {
        Row: infer R
      }
      ? R
      : never
    : never

export type TablesInsert<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Insert: infer I
      }
      ? I
      : never
    : never

export type TablesUpdate<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Update: infer U
      }
      ? U
      : never
    : never

export type Enums<
  DefaultSchemaEnumNameOrOptions extends
    | keyof DefaultSchema["Enums"]
    | { schema: keyof DatabaseWithoutInternals },
  EnumName extends DefaultSchemaEnumNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"]
    : never = never,
> = DefaultSchemaEnumNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : DefaultSchemaEnumNameOrOptions extends keyof DefaultSchema["Enums"]
    ? DefaultSchema["Enums"][DefaultSchemaEnumNameOrOptions]
    : never

export type CompositeTypes<
  PublicCompositeTypeNameOrOptions extends
    | keyof DefaultSchema["CompositeTypes"]
    | { schema: keyof DatabaseWithoutInternals },
  CompositeTypeName extends PublicCompositeTypeNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"]
    : never = never,
> = PublicCompositeTypeNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"][CompositeTypeName]
  : PublicCompositeTypeNameOrOptions extends keyof DefaultSchema["CompositeTypes"]
    ? DefaultSchema["CompositeTypes"][PublicCompositeTypeNameOrOptions]
    : never

export const Constants = {
  public: {
    Enums: {},
  },
} as const
