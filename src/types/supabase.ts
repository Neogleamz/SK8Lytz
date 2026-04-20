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
      admin_audit_logs: {
        Row: {
          action: string
          admin_id: string
          created_at: string
          id: string
          reason: string | null
          target_user_id: string
        }
        Insert: {
          action: string
          admin_id: string
          created_at?: string
          id?: string
          reason?: string | null
          target_user_id: string
        }
        Update: {
          action?: string
          admin_id?: string
          created_at?: string
          id?: string
          reason?: string | null
          target_user_id?: string
        }
        Relationships: []
      }
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
          avg_speed_mph: number | null
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
          top_speed_mph: number | null
          total_distance_miles: number | null
          updated_at: string | null
        }
        Insert: {
          avg_speed_mph?: number | null
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
          top_speed_mph?: number | null
          total_distance_miles?: number | null
          updated_at?: string | null
        }
        Update: {
          avg_speed_mph?: number | null
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
          top_speed_mph?: number | null
          total_distance_miles?: number | null
          updated_at?: string | null
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
          updated_at: string | null
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
          updated_at?: string | null
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
          updated_at?: string | null
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
      daemon_status: {
        Row: {
          current_target: string | null
          id: string
          last_error: string | null
          last_heartbeat: string | null
          status: string
          total_denials: number
          total_enriched: number
        }
        Insert: {
          current_target?: string | null
          id?: string
          last_error?: string | null
          last_heartbeat?: string | null
          status?: string
          total_denials?: number
          total_enriched?: number
        }
        Update: {
          current_target?: string | null
          id?: string
          last_error?: string | null
          last_heartbeat?: string | null
          status?: string
          total_denials?: number
          total_enriched?: number
        }
        Relationships: []
      }
      discovered_devices_telemetry: {
        Row: {
          ble_version: number | null
          device_mac: string
          discovered_at: string | null
          firmware_ver: number | null
          id: string
          is_claimed: boolean | null
          led_version: number | null
          location: unknown
          manufacturer_data: string | null
          product_id: number | null
          product_type: string | null
          rssi: number | null
        }
        Insert: {
          ble_version?: number | null
          device_mac: string
          discovered_at?: string | null
          firmware_ver?: number | null
          id?: string
          is_claimed?: boolean | null
          led_version?: number | null
          location?: unknown
          manufacturer_data?: string | null
          product_id?: number | null
          product_type?: string | null
          rssi?: number | null
        }
        Update: {
          ble_version?: number | null
          device_mac?: string
          discovered_at?: string | null
          firmware_ver?: number | null
          id?: string
          is_claimed?: boolean | null
          led_version?: number | null
          location?: unknown
          manufacturer_data?: string | null
          product_id?: number | null
          product_type?: string | null
          rssi?: number | null
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
          rop_lead_time_days: number | null
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
          rop_lead_time_days?: number | null
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
          rop_lead_time_days?: number | null
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
      product_catalog: {
        Row: {
          default_color_sorting: number
          default_ic_type: number
          default_led_points: number
          default_segments: number
          detect_max_points: number
          detect_min_points: number
          display_name: string
          id: string
          is_active: boolean
          updated_at: string | null
          viz_base_height: number
          viz_base_width: number
          viz_blob_diameter_mm: number
          viz_default_points: number
          viz_shape: string
          viz_strip_count: number | null
          viz_strip_orientation: string | null
          viz_strip_separation: number | null
        }
        Insert: {
          default_color_sorting?: number
          default_ic_type?: number
          default_led_points: number
          default_segments?: number
          detect_max_points: number
          detect_min_points: number
          display_name: string
          id: string
          is_active?: boolean
          updated_at?: string | null
          viz_base_height?: number
          viz_base_width?: number
          viz_blob_diameter_mm?: number
          viz_default_points: number
          viz_shape?: string
          viz_strip_count?: number | null
          viz_strip_orientation?: string | null
          viz_strip_separation?: number | null
        }
        Update: {
          default_color_sorting?: number
          default_ic_type?: number
          default_led_points?: number
          default_segments?: number
          detect_max_points?: number
          detect_min_points?: number
          display_name?: string
          id?: string
          is_active?: boolean
          updated_at?: string | null
          viz_base_height?: number
          viz_base_width?: number
          viz_blob_diameter_mm?: number
          viz_default_points?: number
          viz_shape?: string
          viz_strip_count?: number | null
          viz_strip_orientation?: string | null
          viz_strip_separation?: number | null
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
          makeup_fee: number | null
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
          makeup_fee?: number | null
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
          makeup_fee?: number | null
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
          ble_version: number | null
          color_sorting: string | null
          created_at: string
          custom_name: string
          device_mac: string | null
          device_name: string | null
          factory_name: string | null
          firmware_ver: number | null
          group_id: string
          group_name: string | null
          ic_type: string | null
          id: string
          is_pending_sync: boolean | null
          led_points: number | null
          led_version: number | null
          manufacturer_data: string | null
          points: number
          position: string | null
          product_id: number | null
          product_type: string | null
          registered_at: string | null
          rf_mode: string | null
          rf_paired_count: number | null
          rssi_at_register: number | null
          segments: number
          sorting: string
          strip_type: string
          updated_at: string | null
          user_id: string
        }
        Insert: {
          ble_version?: number | null
          color_sorting?: string | null
          created_at?: string
          custom_name: string
          device_mac?: string | null
          device_name?: string | null
          factory_name?: string | null
          firmware_ver?: number | null
          group_id: string
          group_name?: string | null
          ic_type?: string | null
          id: string
          is_pending_sync?: boolean | null
          led_points?: number | null
          led_version?: number | null
          manufacturer_data?: string | null
          points: number
          position?: string | null
          product_id?: number | null
          product_type?: string | null
          registered_at?: string | null
          rf_mode?: string | null
          rf_paired_count?: number | null
          rssi_at_register?: number | null
          segments: number
          sorting: string
          strip_type: string
          updated_at?: string | null
          user_id: string
        }
        Update: {
          ble_version?: number | null
          color_sorting?: string | null
          created_at?: string
          custom_name?: string
          device_mac?: string | null
          device_name?: string | null
          factory_name?: string | null
          firmware_ver?: number | null
          group_id?: string
          group_name?: string | null
          ic_type?: string | null
          id?: string
          is_pending_sync?: boolean | null
          led_points?: number | null
          led_version?: number | null
          manufacturer_data?: string | null
          points?: number
          position?: string | null
          product_id?: number | null
          product_type?: string | null
          registered_at?: string | null
          rf_mode?: string | null
          rf_paired_count?: number | null
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
      scraper_config: {
        Row: {
          auto_resume_enabled: boolean | null
          cooldown_base_ms: number | null
          cooldown_jitter_pct: number | null
          id: number
          identity_rotation_enabled: boolean | null
          is_active: boolean | null
          max_consecutive_errors: number | null
          randomize_viewport_enabled: boolean | null
          sleep_interval_ms: number | null
          state_override: string[] | null
          target_facilities: string[] | null
          updated_at: string | null
        }
        Insert: {
          auto_resume_enabled?: boolean | null
          cooldown_base_ms?: number | null
          cooldown_jitter_pct?: number | null
          id?: number
          identity_rotation_enabled?: boolean | null
          is_active?: boolean | null
          max_consecutive_errors?: number | null
          randomize_viewport_enabled?: boolean | null
          sleep_interval_ms?: number | null
          state_override?: string[] | null
          target_facilities?: string[] | null
          updated_at?: string | null
        }
        Update: {
          auto_resume_enabled?: boolean | null
          cooldown_base_ms?: number | null
          cooldown_jitter_pct?: number | null
          id?: number
          identity_rotation_enabled?: boolean | null
          is_active?: boolean | null
          max_consecutive_errors?: number | null
          randomize_viewport_enabled?: boolean | null
          sleep_interval_ms?: number | null
          state_override?: string[] | null
          target_facilities?: string[] | null
          updated_at?: string | null
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
      sk8lytz_app_settings: {
        Row: {
          created_at: string
          setting_key: string
          setting_value: Json | null
          updated_at: string
        }
        Insert: {
          created_at?: string
          setting_key: string
          setting_value?: Json | null
          updated_at?: string
        }
        Update: {
          created_at?: string
          setting_key?: string
          setting_value?: Json | null
          updated_at?: string
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
      skate_sessions: {
        Row: {
          avg_speed_mph: number
          calories: number | null
          crew_session_id: string | null
          distance_miles: number
          duration_sec: number
          id: string
          location_label: string | null
          peak_gforce: number | null
          peak_speed_mph: number
          session_date: string
          updated_at: string | null
          user_id: string
        }
        Insert: {
          avg_speed_mph?: number
          calories?: number | null
          crew_session_id?: string | null
          distance_miles?: number
          duration_sec?: number
          id?: string
          location_label?: string | null
          peak_gforce?: number | null
          peak_speed_mph?: number
          session_date?: string
          updated_at?: string | null
          user_id: string
        }
        Update: {
          avg_speed_mph?: number
          calories?: number | null
          crew_session_id?: string | null
          distance_miles?: number
          duration_sec?: number
          id?: string
          location_label?: string | null
          peak_gforce?: number | null
          peak_speed_mph?: number
          session_date?: string
          updated_at?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "skate_sessions_crew_session_id_fkey"
            columns: ["crew_session_id"]
            isOneToOne: false
            referencedRelation: "crew_sessions"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "skate_sessions_crew_session_id_fkey"
            columns: ["crew_session_id"]
            isOneToOne: false
            referencedRelation: "public_sessions"
            referencedColumns: ["id"]
          },
        ]
      }
      skate_spots: {
        Row: {
          address: string | null
          adult_night_details: string | null
          capacity: number | null
          city: string | null
          created_at: string | null
          cultural_metadata: Json | null
          facebook_url: string | null
          facility_type: string | null
          google_place_id: string | null
          has_ac: boolean | null
          has_adult_night: boolean | null
          has_fee: boolean | null
          has_food: boolean | null
          has_lights: boolean | null
          has_lockers: boolean | null
          has_pro_shop: boolean | null
          has_proshop: boolean | null
          has_rental: boolean | null
          has_toilets: boolean | null
          has_wifi: boolean | null
          hosts_derby: boolean | null
          id: string
          instagram_url: string | null
          is_deep_crawled: boolean | null
          is_featured: boolean | null
          is_indoor: boolean | null
          is_published: boolean | null
          is_verified: boolean | null
          is_wheelchair_accessible: boolean | null
          last_attempted_at: string | null
          last_enriched_at: string | null
          lat: number
          lng: number
          name: string
          opening_hours: Json | null
          operator_description: string | null
          operator_name: string | null
          phone: string | null
          phone_number: string | null
          pricing_data: Json | null
          rating: number | null
          raw_knowledge_panel: Json | null
          retry_count: number | null
          schedule_url: string | null
          socials: Json | null
          source: string | null
          special_events: Json | null
          state: string | null
          street_address: string | null
          surface_quality: string | null
          surface_type: Database["public"]["Enums"]["skate_spot_surface"] | null
          tiktok_url: string | null
          updated_at: string | null
          updated_by: string | null
          user_ratings_total: number | null
          verification_status: string | null
          vibe_rating: number | null
          vibe_score: number | null
          website: string | null
          zip: string | null
        }
        Insert: {
          address?: string | null
          adult_night_details?: string | null
          capacity?: number | null
          city?: string | null
          created_at?: string | null
          cultural_metadata?: Json | null
          facebook_url?: string | null
          facility_type?: string | null
          google_place_id?: string | null
          has_ac?: boolean | null
          has_adult_night?: boolean | null
          has_fee?: boolean | null
          has_food?: boolean | null
          has_lights?: boolean | null
          has_lockers?: boolean | null
          has_pro_shop?: boolean | null
          has_proshop?: boolean | null
          has_rental?: boolean | null
          has_toilets?: boolean | null
          has_wifi?: boolean | null
          hosts_derby?: boolean | null
          id?: string
          instagram_url?: string | null
          is_deep_crawled?: boolean | null
          is_featured?: boolean | null
          is_indoor?: boolean | null
          is_published?: boolean | null
          is_verified?: boolean | null
          is_wheelchair_accessible?: boolean | null
          last_attempted_at?: string | null
          last_enriched_at?: string | null
          lat: number
          lng: number
          name: string
          opening_hours?: Json | null
          operator_description?: string | null
          operator_name?: string | null
          phone?: string | null
          phone_number?: string | null
          pricing_data?: Json | null
          rating?: number | null
          raw_knowledge_panel?: Json | null
          retry_count?: number | null
          schedule_url?: string | null
          socials?: Json | null
          source?: string | null
          special_events?: Json | null
          state?: string | null
          street_address?: string | null
          surface_quality?: string | null
          surface_type?:
            | Database["public"]["Enums"]["skate_spot_surface"]
            | null
          tiktok_url?: string | null
          updated_at?: string | null
          updated_by?: string | null
          user_ratings_total?: number | null
          verification_status?: string | null
          vibe_rating?: number | null
          vibe_score?: number | null
          website?: string | null
          zip?: string | null
        }
        Update: {
          address?: string | null
          adult_night_details?: string | null
          capacity?: number | null
          city?: string | null
          created_at?: string | null
          cultural_metadata?: Json | null
          facebook_url?: string | null
          facility_type?: string | null
          google_place_id?: string | null
          has_ac?: boolean | null
          has_adult_night?: boolean | null
          has_fee?: boolean | null
          has_food?: boolean | null
          has_lights?: boolean | null
          has_lockers?: boolean | null
          has_pro_shop?: boolean | null
          has_proshop?: boolean | null
          has_rental?: boolean | null
          has_toilets?: boolean | null
          has_wifi?: boolean | null
          hosts_derby?: boolean | null
          id?: string
          instagram_url?: string | null
          is_deep_crawled?: boolean | null
          is_featured?: boolean | null
          is_indoor?: boolean | null
          is_published?: boolean | null
          is_verified?: boolean | null
          is_wheelchair_accessible?: boolean | null
          last_attempted_at?: string | null
          last_enriched_at?: string | null
          lat?: number
          lng?: number
          name?: string
          opening_hours?: Json | null
          operator_description?: string | null
          operator_name?: string | null
          phone?: string | null
          phone_number?: string | null
          pricing_data?: Json | null
          rating?: number | null
          raw_knowledge_panel?: Json | null
          retry_count?: number | null
          schedule_url?: string | null
          socials?: Json | null
          source?: string | null
          special_events?: Json | null
          state?: string | null
          street_address?: string | null
          surface_quality?: string | null
          surface_type?:
            | Database["public"]["Enums"]["skate_spot_surface"]
            | null
          tiktok_url?: string | null
          updated_at?: string | null
          updated_by?: string | null
          user_ratings_total?: number | null
          verification_status?: string | null
          vibe_rating?: number | null
          vibe_score?: number | null
          website?: string | null
          zip?: string | null
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
      spatial_ref_sys: {
        Row: {
          auth_name: string | null
          auth_srid: number | null
          proj4text: string | null
          srid: number
          srtext: string | null
        }
        Insert: {
          auth_name?: string | null
          auth_srid?: number | null
          proj4text?: string | null
          srid: number
          srtext?: string | null
        }
        Update: {
          auth_name?: string | null
          auth_srid?: number | null
          proj4text?: string | null
          srid?: number
          srtext?: string | null
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
      telemetry_snapshots: {
        Row: {
          created_at: string | null
          device_id: string | null
          event_type: string
          id: string
          metadata: Json | null
          session_id: string
        }
        Insert: {
          created_at?: string | null
          device_id?: string | null
          event_type: string
          id?: string
          metadata?: Json | null
          session_id: string
        }
        Update: {
          created_at?: string | null
          device_id?: string | null
          event_type?: string
          id?: string
          metadata?: Json | null
          session_id?: string
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
          accepted_eula_version: number | null
          avatar_color: string
          avatar_url: string | null
          ban_reason: string | null
          created_at: string
          display_name: string | null
          is_banned: boolean
          lifetime_distance_miles: number | null
          lifetime_top_speed_mph: number | null
          role: Database["public"]["Enums"]["user_role"]
          updated_at: string
          user_id: string
          username: string | null
        }
        Insert: {
          accepted_eula_version?: number | null
          avatar_color?: string
          avatar_url?: string | null
          ban_reason?: string | null
          created_at?: string
          display_name?: string | null
          is_banned?: boolean
          lifetime_distance_miles?: number | null
          lifetime_top_speed_mph?: number | null
          role?: Database["public"]["Enums"]["user_role"]
          updated_at?: string
          user_id: string
          username?: string | null
        }
        Update: {
          accepted_eula_version?: number | null
          avatar_color?: string
          avatar_url?: string | null
          ban_reason?: string | null
          created_at?: string
          display_name?: string | null
          is_banned?: boolean
          lifetime_distance_miles?: number | null
          lifetime_top_speed_mph?: number | null
          role?: Database["public"]["Enums"]["user_role"]
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
      geography_columns: {
        Row: {
          coord_dimension: number | null
          f_geography_column: unknown
          f_table_catalog: unknown
          f_table_name: unknown
          f_table_schema: unknown
          srid: number | null
          type: string | null
        }
        Relationships: []
      }
      geometry_columns: {
        Row: {
          coord_dimension: number | null
          f_geometry_column: unknown
          f_table_catalog: string | null
          f_table_name: unknown
          f_table_schema: unknown
          srid: number | null
          type: string | null
        }
        Insert: {
          coord_dimension?: number | null
          f_geometry_column?: unknown
          f_table_catalog?: string | null
          f_table_name?: unknown
          f_table_schema?: unknown
          srid?: number | null
          type?: string | null
        }
        Update: {
          coord_dimension?: number | null
          f_geometry_column?: unknown
          f_table_catalog?: string | null
          f_table_name?: unknown
          f_table_schema?: unknown
          srid?: number | null
          type?: string | null
        }
        Relationships: []
      }
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
      _postgis_deprecate: {
        Args: { newname: string; oldname: string; version: string }
        Returns: undefined
      }
      _postgis_index_extent: {
        Args: { col: string; tbl: unknown }
        Returns: unknown
      }
      _postgis_pgsql_version: { Args: never; Returns: string }
      _postgis_scripts_pgsql_version: { Args: never; Returns: string }
      _postgis_selectivity: {
        Args: { att_name: string; geom: unknown; mode?: string; tbl: unknown }
        Returns: number
      }
      _postgis_stats: {
        Args: { ""?: string; att_name: string; tbl: unknown }
        Returns: string
      }
      _st_3dintersects: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_contains: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_containsproperly: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_coveredby:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: boolean }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      _st_covers:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: boolean }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      _st_crosses: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_dwithin: {
        Args: {
          geog1: unknown
          geog2: unknown
          tolerance: number
          use_spheroid?: boolean
        }
        Returns: boolean
      }
      _st_equals: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      _st_intersects: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_linecrossingdirection: {
        Args: { line1: unknown; line2: unknown }
        Returns: number
      }
      _st_longestline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      _st_maxdistance: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      _st_orderingequals: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_overlaps: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_sortablehash: { Args: { geom: unknown }; Returns: number }
      _st_touches: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      _st_voronoi: {
        Args: {
          clip?: unknown
          g1: unknown
          return_polygons?: boolean
          tolerance?: number
        }
        Returns: unknown
      }
      _st_within: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      addauth: { Args: { "": string }; Returns: boolean }
      addgeometrycolumn:
        | {
            Args: {
              catalog_name: string
              column_name: string
              new_dim: number
              new_srid_in: number
              new_type: string
              schema_name: string
              table_name: string
              use_typmod?: boolean
            }
            Returns: string
          }
        | {
            Args: {
              column_name: string
              new_dim: number
              new_srid: number
              new_type: string
              schema_name: string
              table_name: string
              use_typmod?: boolean
            }
            Returns: string
          }
        | {
            Args: {
              column_name: string
              new_dim: number
              new_srid: number
              new_type: string
              table_name: string
              use_typmod?: boolean
            }
            Returns: string
          }
      admin_ban_user: {
        Args: { p_reason: string; p_target_user_id: string }
        Returns: undefined
      }
      admin_force_password_reset: {
        Args: { p_target_user_id: string }
        Returns: undefined
      }
      admin_revoke_ban: {
        Args: { p_target_user_id: string }
        Returns: undefined
      }
      admin_soft_delete_user: {
        Args: { p_target_user_id: string }
        Returns: undefined
      }
      delete_group_cascade: { Args: { p_group_id: string }; Returns: undefined }
      disablelongtransactions: { Args: never; Returns: string }
      dropgeometrycolumn:
        | {
            Args: {
              catalog_name: string
              column_name: string
              schema_name: string
              table_name: string
            }
            Returns: string
          }
        | {
            Args: {
              column_name: string
              schema_name: string
              table_name: string
            }
            Returns: string
          }
        | { Args: { column_name: string; table_name: string }; Returns: string }
      dropgeometrytable:
        | {
            Args: {
              catalog_name: string
              schema_name: string
              table_name: string
            }
            Returns: string
          }
        | { Args: { schema_name: string; table_name: string }; Returns: string }
        | { Args: { table_name: string }; Returns: string }
      enablelongtransactions: { Args: never; Returns: string }
      equals: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      geometry: { Args: { "": string }; Returns: unknown }
      geometry_above: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_below: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_cmp: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      geometry_contained_3d: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_contains: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_contains_3d: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_distance_box: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      geometry_distance_centroid: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      geometry_eq: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_ge: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_gt: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_le: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_left: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_lt: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overabove: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overbelow: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overlaps: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overlaps_3d: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overleft: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_overright: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_right: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_same: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_same_3d: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geometry_within: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      geomfromewkt: { Args: { "": string }; Returns: unknown }
      get_closest_skate_spot: {
        Args: { p_lat: number; p_lng: number; p_radius_meters: number }
        Returns: {
          distance_meters: number
          name: string
          spot_id: string
        }[]
      }
      get_email_by_username: { Args: { p_username: string }; Returns: string }
      get_nearby_push_tokens: {
        Args: { p_max_tokens?: number; p_session_id: string }
        Returns: {
          platform: string
          token: string
        }[]
      }
      get_next_spot_for_indexer: {
        Args: never
        Returns: {
          address: string | null
          adult_night_details: string | null
          capacity: number | null
          city: string | null
          created_at: string | null
          cultural_metadata: Json | null
          facebook_url: string | null
          facility_type: string | null
          google_place_id: string | null
          has_ac: boolean | null
          has_adult_night: boolean | null
          has_fee: boolean | null
          has_food: boolean | null
          has_lights: boolean | null
          has_lockers: boolean | null
          has_pro_shop: boolean | null
          has_proshop: boolean | null
          has_rental: boolean | null
          has_toilets: boolean | null
          has_wifi: boolean | null
          hosts_derby: boolean | null
          id: string
          instagram_url: string | null
          is_deep_crawled: boolean | null
          is_featured: boolean | null
          is_indoor: boolean | null
          is_published: boolean | null
          is_verified: boolean | null
          is_wheelchair_accessible: boolean | null
          last_attempted_at: string | null
          last_enriched_at: string | null
          lat: number
          lng: number
          name: string
          opening_hours: Json | null
          operator_description: string | null
          operator_name: string | null
          phone: string | null
          phone_number: string | null
          pricing_data: Json | null
          rating: number | null
          raw_knowledge_panel: Json | null
          retry_count: number | null
          schedule_url: string | null
          socials: Json | null
          source: string | null
          special_events: Json | null
          state: string | null
          street_address: string | null
          surface_quality: string | null
          surface_type: Database["public"]["Enums"]["skate_spot_surface"] | null
          tiktok_url: string | null
          updated_at: string | null
          updated_by: string | null
          user_ratings_total: number | null
          verification_status: string | null
          vibe_rating: number | null
          vibe_score: number | null
          website: string | null
          zip: string | null
        }[]
        SetofOptions: {
          from: "*"
          to: "skate_spots"
          isOneToOne: false
          isSetofReturn: true
        }
      }
      get_next_spot_for_operator: {
        Args: never
        Returns: {
          address: string | null
          adult_night_details: string | null
          capacity: number | null
          city: string | null
          created_at: string | null
          cultural_metadata: Json | null
          facebook_url: string | null
          facility_type: string | null
          google_place_id: string | null
          has_ac: boolean | null
          has_adult_night: boolean | null
          has_fee: boolean | null
          has_food: boolean | null
          has_lights: boolean | null
          has_lockers: boolean | null
          has_pro_shop: boolean | null
          has_proshop: boolean | null
          has_rental: boolean | null
          has_toilets: boolean | null
          has_wifi: boolean | null
          hosts_derby: boolean | null
          id: string
          instagram_url: string | null
          is_deep_crawled: boolean | null
          is_featured: boolean | null
          is_indoor: boolean | null
          is_published: boolean | null
          is_verified: boolean | null
          is_wheelchair_accessible: boolean | null
          last_attempted_at: string | null
          last_enriched_at: string | null
          lat: number
          lng: number
          name: string
          opening_hours: Json | null
          operator_description: string | null
          operator_name: string | null
          phone: string | null
          phone_number: string | null
          pricing_data: Json | null
          rating: number | null
          raw_knowledge_panel: Json | null
          retry_count: number | null
          schedule_url: string | null
          socials: Json | null
          source: string | null
          special_events: Json | null
          state: string | null
          street_address: string | null
          surface_quality: string | null
          surface_type: Database["public"]["Enums"]["skate_spot_surface"] | null
          tiktok_url: string | null
          updated_at: string | null
          updated_by: string | null
          user_ratings_total: number | null
          verification_status: string | null
          vibe_rating: number | null
          vibe_score: number | null
          website: string | null
          zip: string | null
        }[]
        SetofOptions: {
          from: "*"
          to: "skate_spots"
          isOneToOne: false
          isSetofReturn: true
        }
      }
      get_next_spot_to_enrich: {
        Args: never
        Returns: {
          address: string | null
          adult_night_details: string | null
          capacity: number | null
          city: string | null
          created_at: string | null
          cultural_metadata: Json | null
          facebook_url: string | null
          facility_type: string | null
          google_place_id: string | null
          has_ac: boolean | null
          has_adult_night: boolean | null
          has_fee: boolean | null
          has_food: boolean | null
          has_lights: boolean | null
          has_lockers: boolean | null
          has_pro_shop: boolean | null
          has_proshop: boolean | null
          has_rental: boolean | null
          has_toilets: boolean | null
          has_wifi: boolean | null
          hosts_derby: boolean | null
          id: string
          instagram_url: string | null
          is_deep_crawled: boolean | null
          is_featured: boolean | null
          is_indoor: boolean | null
          is_published: boolean | null
          is_verified: boolean | null
          is_wheelchair_accessible: boolean | null
          last_attempted_at: string | null
          last_enriched_at: string | null
          lat: number
          lng: number
          name: string
          opening_hours: Json | null
          operator_description: string | null
          operator_name: string | null
          phone: string | null
          phone_number: string | null
          pricing_data: Json | null
          rating: number | null
          raw_knowledge_panel: Json | null
          retry_count: number | null
          schedule_url: string | null
          socials: Json | null
          source: string | null
          special_events: Json | null
          state: string | null
          street_address: string | null
          surface_quality: string | null
          surface_type: Database["public"]["Enums"]["skate_spot_surface"] | null
          tiktok_url: string | null
          updated_at: string | null
          updated_by: string | null
          user_ratings_total: number | null
          verification_status: string | null
          vibe_rating: number | null
          vibe_score: number | null
          website: string | null
          zip: string | null
        }[]
        SetofOptions: {
          from: "*"
          to: "skate_spots"
          isOneToOne: false
          isSetofReturn: true
        }
      }
      gettransactionid: { Args: never; Returns: unknown }
      increment_scene_download: {
        Args: { scene_id: string }
        Returns: undefined
      }
      increment_scene_upvote: { Args: { scene_id: string }; Returns: undefined }
      longtransactionsenabled: { Args: never; Returns: boolean }
      populate_geometry_columns:
        | { Args: { tbl_oid: unknown; use_typmod?: boolean }; Returns: number }
        | { Args: { use_typmod?: boolean }; Returns: string }
      postgis_constraint_dims: {
        Args: { geomcolumn: string; geomschema: string; geomtable: string }
        Returns: number
      }
      postgis_constraint_srid: {
        Args: { geomcolumn: string; geomschema: string; geomtable: string }
        Returns: number
      }
      postgis_constraint_type: {
        Args: { geomcolumn: string; geomschema: string; geomtable: string }
        Returns: string
      }
      postgis_extensions_upgrade: { Args: never; Returns: string }
      postgis_full_version: { Args: never; Returns: string }
      postgis_geos_version: { Args: never; Returns: string }
      postgis_lib_build_date: { Args: never; Returns: string }
      postgis_lib_revision: { Args: never; Returns: string }
      postgis_lib_version: { Args: never; Returns: string }
      postgis_libjson_version: { Args: never; Returns: string }
      postgis_liblwgeom_version: { Args: never; Returns: string }
      postgis_libprotobuf_version: { Args: never; Returns: string }
      postgis_libxml_version: { Args: never; Returns: string }
      postgis_proj_version: { Args: never; Returns: string }
      postgis_scripts_build_date: { Args: never; Returns: string }
      postgis_scripts_installed: { Args: never; Returns: string }
      postgis_scripts_released: { Args: never; Returns: string }
      postgis_svn_version: { Args: never; Returns: string }
      postgis_type_name: {
        Args: {
          coord_dimension: number
          geomname: string
          use_new_name?: boolean
        }
        Returns: string
      }
      postgis_version: { Args: never; Returns: string }
      postgis_wagyu_version: { Args: never; Returns: string }
      st_3dclosestpoint: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_3ddistance: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_3dintersects: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_3dlongestline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_3dmakebox: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_3dmaxdistance: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_3dshortestline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_addpoint: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_angle:
        | { Args: { line1: unknown; line2: unknown }; Returns: number }
        | {
            Args: { pt1: unknown; pt2: unknown; pt3: unknown; pt4?: unknown }
            Returns: number
          }
      st_area:
        | { Args: { geog: unknown; use_spheroid?: boolean }; Returns: number }
        | { Args: { "": string }; Returns: number }
      st_asencodedpolyline: {
        Args: { geom: unknown; nprecision?: number }
        Returns: string
      }
      st_asewkt: { Args: { "": string }; Returns: string }
      st_asgeojson:
        | {
            Args: { geog: unknown; maxdecimaldigits?: number; options?: number }
            Returns: string
          }
        | {
            Args: { geom: unknown; maxdecimaldigits?: number; options?: number }
            Returns: string
          }
        | {
            Args: {
              geom_column?: string
              maxdecimaldigits?: number
              pretty_bool?: boolean
              r: Record<string, unknown>
            }
            Returns: string
          }
        | { Args: { "": string }; Returns: string }
      st_asgml:
        | {
            Args: {
              geog: unknown
              id?: string
              maxdecimaldigits?: number
              nprefix?: string
              options?: number
            }
            Returns: string
          }
        | {
            Args: { geom: unknown; maxdecimaldigits?: number; options?: number }
            Returns: string
          }
        | { Args: { "": string }; Returns: string }
        | {
            Args: {
              geog: unknown
              id?: string
              maxdecimaldigits?: number
              nprefix?: string
              options?: number
              version: number
            }
            Returns: string
          }
        | {
            Args: {
              geom: unknown
              id?: string
              maxdecimaldigits?: number
              nprefix?: string
              options?: number
              version: number
            }
            Returns: string
          }
      st_askml:
        | {
            Args: { geog: unknown; maxdecimaldigits?: number; nprefix?: string }
            Returns: string
          }
        | {
            Args: { geom: unknown; maxdecimaldigits?: number; nprefix?: string }
            Returns: string
          }
        | { Args: { "": string }; Returns: string }
      st_aslatlontext: {
        Args: { geom: unknown; tmpl?: string }
        Returns: string
      }
      st_asmarc21: { Args: { format?: string; geom: unknown }; Returns: string }
      st_asmvtgeom: {
        Args: {
          bounds: unknown
          buffer?: number
          clip_geom?: boolean
          extent?: number
          geom: unknown
        }
        Returns: unknown
      }
      st_assvg:
        | {
            Args: { geog: unknown; maxdecimaldigits?: number; rel?: number }
            Returns: string
          }
        | {
            Args: { geom: unknown; maxdecimaldigits?: number; rel?: number }
            Returns: string
          }
        | { Args: { "": string }; Returns: string }
      st_astext: { Args: { "": string }; Returns: string }
      st_astwkb:
        | {
            Args: {
              geom: unknown
              prec?: number
              prec_m?: number
              prec_z?: number
              with_boxes?: boolean
              with_sizes?: boolean
            }
            Returns: string
          }
        | {
            Args: {
              geom: unknown[]
              ids: number[]
              prec?: number
              prec_m?: number
              prec_z?: number
              with_boxes?: boolean
              with_sizes?: boolean
            }
            Returns: string
          }
      st_asx3d: {
        Args: { geom: unknown; maxdecimaldigits?: number; options?: number }
        Returns: string
      }
      st_azimuth:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: number }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: number }
      st_boundingdiagonal: {
        Args: { fits?: boolean; geom: unknown }
        Returns: unknown
      }
      st_buffer:
        | {
            Args: { geom: unknown; options?: string; radius: number }
            Returns: unknown
          }
        | {
            Args: { geom: unknown; quadsegs: number; radius: number }
            Returns: unknown
          }
      st_centroid: { Args: { "": string }; Returns: unknown }
      st_clipbybox2d: {
        Args: { box: unknown; geom: unknown }
        Returns: unknown
      }
      st_closestpoint: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_collect: { Args: { geom1: unknown; geom2: unknown }; Returns: unknown }
      st_concavehull: {
        Args: {
          param_allow_holes?: boolean
          param_geom: unknown
          param_pctconvex: number
        }
        Returns: unknown
      }
      st_contains: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_containsproperly: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_coorddim: { Args: { geometry: unknown }; Returns: number }
      st_coveredby:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: boolean }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_covers:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: boolean }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_crosses: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_curvetoline: {
        Args: { flags?: number; geom: unknown; tol?: number; toltype?: number }
        Returns: unknown
      }
      st_delaunaytriangles: {
        Args: { flags?: number; g1: unknown; tolerance?: number }
        Returns: unknown
      }
      st_difference: {
        Args: { geom1: unknown; geom2: unknown; gridsize?: number }
        Returns: unknown
      }
      st_disjoint: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_distance:
        | {
            Args: { geog1: unknown; geog2: unknown; use_spheroid?: boolean }
            Returns: number
          }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: number }
      st_distancesphere:
        | { Args: { geom1: unknown; geom2: unknown }; Returns: number }
        | {
            Args: { geom1: unknown; geom2: unknown; radius: number }
            Returns: number
          }
      st_distancespheroid: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_dwithin: {
        Args: {
          geog1: unknown
          geog2: unknown
          tolerance: number
          use_spheroid?: boolean
        }
        Returns: boolean
      }
      st_equals: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_expand:
        | { Args: { box: unknown; dx: number; dy: number }; Returns: unknown }
        | {
            Args: { box: unknown; dx: number; dy: number; dz?: number }
            Returns: unknown
          }
        | {
            Args: {
              dm?: number
              dx: number
              dy: number
              dz?: number
              geom: unknown
            }
            Returns: unknown
          }
      st_force3d: { Args: { geom: unknown; zvalue?: number }; Returns: unknown }
      st_force3dm: {
        Args: { geom: unknown; mvalue?: number }
        Returns: unknown
      }
      st_force3dz: {
        Args: { geom: unknown; zvalue?: number }
        Returns: unknown
      }
      st_force4d: {
        Args: { geom: unknown; mvalue?: number; zvalue?: number }
        Returns: unknown
      }
      st_generatepoints:
        | { Args: { area: unknown; npoints: number }; Returns: unknown }
        | {
            Args: { area: unknown; npoints: number; seed: number }
            Returns: unknown
          }
      st_geogfromtext: { Args: { "": string }; Returns: unknown }
      st_geographyfromtext: { Args: { "": string }; Returns: unknown }
      st_geohash:
        | { Args: { geog: unknown; maxchars?: number }; Returns: string }
        | { Args: { geom: unknown; maxchars?: number }; Returns: string }
      st_geomcollfromtext: { Args: { "": string }; Returns: unknown }
      st_geometricmedian: {
        Args: {
          fail_if_not_converged?: boolean
          g: unknown
          max_iter?: number
          tolerance?: number
        }
        Returns: unknown
      }
      st_geometryfromtext: { Args: { "": string }; Returns: unknown }
      st_geomfromewkt: { Args: { "": string }; Returns: unknown }
      st_geomfromgeojson:
        | { Args: { "": Json }; Returns: unknown }
        | { Args: { "": Json }; Returns: unknown }
        | { Args: { "": string }; Returns: unknown }
      st_geomfromgml: { Args: { "": string }; Returns: unknown }
      st_geomfromkml: { Args: { "": string }; Returns: unknown }
      st_geomfrommarc21: { Args: { marc21xml: string }; Returns: unknown }
      st_geomfromtext: { Args: { "": string }; Returns: unknown }
      st_gmltosql: { Args: { "": string }; Returns: unknown }
      st_hasarc: { Args: { geometry: unknown }; Returns: boolean }
      st_hausdorffdistance: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_hexagon: {
        Args: { cell_i: number; cell_j: number; origin?: unknown; size: number }
        Returns: unknown
      }
      st_hexagongrid: {
        Args: { bounds: unknown; size: number }
        Returns: Record<string, unknown>[]
      }
      st_interpolatepoint: {
        Args: { line: unknown; point: unknown }
        Returns: number
      }
      st_intersection: {
        Args: { geom1: unknown; geom2: unknown; gridsize?: number }
        Returns: unknown
      }
      st_intersects:
        | { Args: { geog1: unknown; geog2: unknown }; Returns: boolean }
        | { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_isvaliddetail: {
        Args: { flags?: number; geom: unknown }
        Returns: Database["public"]["CompositeTypes"]["valid_detail"]
        SetofOptions: {
          from: "*"
          to: "valid_detail"
          isOneToOne: true
          isSetofReturn: false
        }
      }
      st_length:
        | { Args: { geog: unknown; use_spheroid?: boolean }; Returns: number }
        | { Args: { "": string }; Returns: number }
      st_letters: { Args: { font?: Json; letters: string }; Returns: unknown }
      st_linecrossingdirection: {
        Args: { line1: unknown; line2: unknown }
        Returns: number
      }
      st_linefromencodedpolyline: {
        Args: { nprecision?: number; txtin: string }
        Returns: unknown
      }
      st_linefromtext: { Args: { "": string }; Returns: unknown }
      st_linelocatepoint: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_linetocurve: { Args: { geometry: unknown }; Returns: unknown }
      st_locatealong: {
        Args: { geometry: unknown; leftrightoffset?: number; measure: number }
        Returns: unknown
      }
      st_locatebetween: {
        Args: {
          frommeasure: number
          geometry: unknown
          leftrightoffset?: number
          tomeasure: number
        }
        Returns: unknown
      }
      st_locatebetweenelevations: {
        Args: { fromelevation: number; geometry: unknown; toelevation: number }
        Returns: unknown
      }
      st_longestline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_makebox2d: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_makeline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_makevalid: {
        Args: { geom: unknown; params: string }
        Returns: unknown
      }
      st_maxdistance: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: number
      }
      st_minimumboundingcircle: {
        Args: { inputgeom: unknown; segs_per_quarter?: number }
        Returns: unknown
      }
      st_mlinefromtext: { Args: { "": string }; Returns: unknown }
      st_mpointfromtext: { Args: { "": string }; Returns: unknown }
      st_mpolyfromtext: { Args: { "": string }; Returns: unknown }
      st_multilinestringfromtext: { Args: { "": string }; Returns: unknown }
      st_multipointfromtext: { Args: { "": string }; Returns: unknown }
      st_multipolygonfromtext: { Args: { "": string }; Returns: unknown }
      st_node: { Args: { g: unknown }; Returns: unknown }
      st_normalize: { Args: { geom: unknown }; Returns: unknown }
      st_offsetcurve: {
        Args: { distance: number; line: unknown; params?: string }
        Returns: unknown
      }
      st_orderingequals: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_overlaps: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: boolean
      }
      st_perimeter: {
        Args: { geog: unknown; use_spheroid?: boolean }
        Returns: number
      }
      st_pointfromtext: { Args: { "": string }; Returns: unknown }
      st_pointm: {
        Args: {
          mcoordinate: number
          srid?: number
          xcoordinate: number
          ycoordinate: number
        }
        Returns: unknown
      }
      st_pointz: {
        Args: {
          srid?: number
          xcoordinate: number
          ycoordinate: number
          zcoordinate: number
        }
        Returns: unknown
      }
      st_pointzm: {
        Args: {
          mcoordinate: number
          srid?: number
          xcoordinate: number
          ycoordinate: number
          zcoordinate: number
        }
        Returns: unknown
      }
      st_polyfromtext: { Args: { "": string }; Returns: unknown }
      st_polygonfromtext: { Args: { "": string }; Returns: unknown }
      st_project: {
        Args: { azimuth: number; distance: number; geog: unknown }
        Returns: unknown
      }
      st_quantizecoordinates: {
        Args: {
          g: unknown
          prec_m?: number
          prec_x: number
          prec_y?: number
          prec_z?: number
        }
        Returns: unknown
      }
      st_reduceprecision: {
        Args: { geom: unknown; gridsize: number }
        Returns: unknown
      }
      st_relate: { Args: { geom1: unknown; geom2: unknown }; Returns: string }
      st_removerepeatedpoints: {
        Args: { geom: unknown; tolerance?: number }
        Returns: unknown
      }
      st_segmentize: {
        Args: { geog: unknown; max_segment_length: number }
        Returns: unknown
      }
      st_setsrid:
        | { Args: { geog: unknown; srid: number }; Returns: unknown }
        | { Args: { geom: unknown; srid: number }; Returns: unknown }
      st_sharedpaths: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_shortestline: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_simplifypolygonhull: {
        Args: { geom: unknown; is_outer?: boolean; vertex_fraction: number }
        Returns: unknown
      }
      st_split: { Args: { geom1: unknown; geom2: unknown }; Returns: unknown }
      st_square: {
        Args: { cell_i: number; cell_j: number; origin?: unknown; size: number }
        Returns: unknown
      }
      st_squaregrid: {
        Args: { bounds: unknown; size: number }
        Returns: Record<string, unknown>[]
      }
      st_srid:
        | { Args: { geog: unknown }; Returns: number }
        | { Args: { geom: unknown }; Returns: number }
      st_subdivide: {
        Args: { geom: unknown; gridsize?: number; maxvertices?: number }
        Returns: unknown[]
      }
      st_swapordinates: {
        Args: { geom: unknown; ords: unknown }
        Returns: unknown
      }
      st_symdifference: {
        Args: { geom1: unknown; geom2: unknown; gridsize?: number }
        Returns: unknown
      }
      st_symmetricdifference: {
        Args: { geom1: unknown; geom2: unknown }
        Returns: unknown
      }
      st_tileenvelope: {
        Args: {
          bounds?: unknown
          margin?: number
          x: number
          y: number
          zoom: number
        }
        Returns: unknown
      }
      st_touches: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_transform:
        | {
            Args: { from_proj: string; geom: unknown; to_proj: string }
            Returns: unknown
          }
        | {
            Args: { from_proj: string; geom: unknown; to_srid: number }
            Returns: unknown
          }
        | { Args: { geom: unknown; to_proj: string }; Returns: unknown }
      st_triangulatepolygon: { Args: { g1: unknown }; Returns: unknown }
      st_union:
        | { Args: { geom1: unknown; geom2: unknown }; Returns: unknown }
        | {
            Args: { geom1: unknown; geom2: unknown; gridsize: number }
            Returns: unknown
          }
      st_voronoilines: {
        Args: { extend_to?: unknown; g1: unknown; tolerance?: number }
        Returns: unknown
      }
      st_voronoipolygons: {
        Args: { extend_to?: unknown; g1: unknown; tolerance?: number }
        Returns: unknown
      }
      st_within: { Args: { geom1: unknown; geom2: unknown }; Returns: boolean }
      st_wkbtosql: { Args: { wkb: string }; Returns: unknown }
      st_wkttosql: { Args: { "": string }; Returns: unknown }
      st_wrapx: {
        Args: { geom: unknown; move: number; wrap: number }
        Returns: unknown
      }
      unlockrows: { Args: { "": string }; Returns: number }
      updategeometrysrid: {
        Args: {
          catalogn_name: string
          column_name: string
          new_srid_in: number
          schema_name: string
          table_name: string
        }
        Returns: string
      }
      upsert_group_with_devices: {
        Args: {
          p_device_ids: string[]
          p_group_id: string
          p_group_name: string
          p_type: string
        }
        Returns: undefined
      }
    }
    Enums: {
      skate_spot_surface:
        | "wood"
        | "concrete"
        | "asphalt"
        | "sport_court"
        | "unknown"
      surface_type: "wood" | "concrete" | "asphalt" | "sport_court" | "unknown"
      user_role: "user" | "moderator" | "admin"
    }
    CompositeTypes: {
      geometry_dump: {
        path: number[] | null
        geom: unknown
      }
      valid_detail: {
        valid: boolean | null
        reason: string | null
        location: unknown
      }
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
    Enums: {
      skate_spot_surface: [
        "wood",
        "concrete",
        "asphalt",
        "sport_court",
        "unknown",
      ],
      surface_type: ["wood", "concrete", "asphalt", "sport_court", "unknown"],
      user_role: ["user", "moderator", "admin"],
    },
  },
} as const

